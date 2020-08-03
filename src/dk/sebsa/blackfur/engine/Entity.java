package dk.sebsa.blackfur.engine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.system.CallbackI.V;

import dk.sebsa.blackfur.math.Matrix4x4;
import dk.sebsa.blackfur.math.Vector2f;

public class Entity {
	public boolean enabled = true;
	private int i;
	
	public String tag = "Untagged";
	public String name = "New Entity";
	
	private Vector2f position = new Vector2f();
	private Vector2f scale = new Vector2f(1, 1);
	private float rotation = 0;
	private Matrix4x4 matrix = new Matrix4x4();
	
	private Entity parent;
	private List<Entity> children = new ArrayList<Entity>();
	private List<Component> components = new ArrayList<Component>();
	private int inline = 0;
	private String id;
	
	private byte dirty = 1;
	private boolean expanded = false;
	private static Entity master = new Entity(false);
	
	private static List<Entity> instances = new ArrayList<Entity>();
	private static int h;
	
	public Entity(boolean addToHierarchy) {
		id = UUID.randomUUID().toString();
		if(!addToHierarchy) {
			inline = -1;
			expanded = true;
			return;
		}
		
		instances.add(this);
		parent(master);
	}
	
	public Entity(String name) {
		id = UUID.randomUUID().toString();
		this.name = name;
		instances.add(this);
		parent(master);
	}
	
	public final boolean isDirty() {
		return dirty == 1;
	}
	
	public void updateMatrix() {
		matrix.setTransform(position, rotation);
	}
	
	public final Matrix4x4 getMatrix() {
		return matrix;
	}
	
	public void parent(Entity e) {
		if(e==null) e = master;
		
		if(parent != null) {
			if(!parent.equals(e)) parent.removeChild(this);
			else return;
		}
		
		parent = e;
		parent.children.add(this);
		setInline(parent.getInline() + 1);
	}
	
	public Component getComponent(String name) {
		for(i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	
	public void removeComponent(Component c) {
		components.remove(c);
	}
	
	public static void clear() {
		instances.clear();
	}
	
	public Component addComponent(String c) {
		Class<?> cls;
		try {
			cls = Class.forName(c);
			try { return addComponent((Component) cls.getConstructor().newInstance()); }
			catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{ Debug.log("Instance of " + c + " could not be created"); }
		} catch (ClassNotFoundException e) { Debug.log("Class " + c + " could not be found"); }
		return null;
	}
	
	public Component addComponent(Component c) {
		if(c == null) { Debug.log("Could not findt component!"); return null; }
		c.init(this);
		components.add(c);
		return c;
	}
	
	public void removeChild(Entity e) {
		for(i = 0; i < children.size(); i++) {
			if(children.get(i)==e) {
				removeChild(i);
				return;
			}
		}
	}
	
	public void removeChild(int v) {
		if(v >= children.size()) return;
		Entity e = children.get(v);
		e.parent(master);
		children.remove(v);
		e.inline = 0;
	}

	public int getInline() {
		return inline;
	}

	public void setInline(int inline) {
		this.inline = inline;
		for(i = 0; i < children.size(); i++) {
			children.get(i).setInline(inline+1);
		}
	}
	
	public static Entity find(String s) {
		for(h = 0; h < instances.size(); h++ ) {
			Entity e = instances.get(h);
			if(e.name.equals(s)) return e;
		}
		return null;
	}

	public Entity getParent() {
		return parent;
	}

	public List<Entity> getChildren() {
		return children;
	}

	public String getId() {
		return id;
	}

	public static Entity master() {
		return master;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<Component> getComponents() {
		return components;
	}

	public final Vector2f getPosition() { return position; }
	public void setPosition(Vector2f p) {
		position.set(p);
		dirty = 1;
	}
	public void setPosition(float x, float y) {
		position.set(x, y);
		dirty = 1;
	}
	
	public final Vector2f getScale() { return scale; }
	public void setScale(Vector2f s) { scale.set(s); }
	public void setScale(float x, float y) { scale.set(x, y); }

	public final float getRotation() { return rotation; }
	public void setRotation(float r)  {
		rotation = r;
		dirty = 1;
	}
	
	public void prepare() {
		for(i = 0; i < components.size(); i++) {
			components.get(i).prepare();
		}
	}
	
	public static void prepareAll() {
		for(h = 0; h < instances.size(); h++) {
			instances.get(h).prepare();
		}
	}
}
