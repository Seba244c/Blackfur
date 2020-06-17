package io.github.seba244c.blackfur.engine;

public abstract class Component {
	public boolean enabled = true;
	
	private String name = "";
	private Entity owner;
	
	public Component() {
		name = getClass().getSimpleName();
	}
	
	public void init(Entity owner) {
		if(this.owner==null)
			this.owner = owner;
	}
	
	public void awake() {}
	public void update() {}
	public void onWillRender() {}
	public void onGUI() {}
	
	public void print(String s) { Debug.Log(s); }
	public void print(int i) { Debug.Log(i); }
	public void print(boolean b) { Debug.Log(b); }
	public void print(float f) { Debug.Log(f); }
	
	public final Entity getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}
}
