package io.github.seba244c.blackfur.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
	public boolean enabled = true;
	private int i;
	
	public String tag = "Untagged";
	public String name = "New Entity";
	
	private Entity parent;
	private List<Entity> children = new ArrayList<Entity>();
	private int inline = 0;
	private String id;
	
	// For editor
	private boolean expanded = false;
	private static Entity master = new Entity(false);
	
	private static List<Entity> instances = new ArrayList<Entity>();
	
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
	
	public void parent(Entity e) {
		if(e==null) e = master;
		
		if(parent != null) {
			if(parent!=e) parent.removeChild(this);
			else return;
		}
		
		parent = e;
		parent.children.add(this);
		setInline(parent.getInline() + 1);
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
}
