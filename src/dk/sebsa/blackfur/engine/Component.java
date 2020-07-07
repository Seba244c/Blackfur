package dk.sebsa.blackfur.engine;

public abstract class Component {
	public boolean enabled = true;
	
	private String name = "";
	protected Entity entity;
	
	public Component() {
		name = getClass().getSimpleName();
	}
	
	public void init(Entity owner) {
		if(this.entity==null)
			this.entity = owner;
	}
	
	public void awake() {
		// When the component is intiliazed
	}
	public void update() {
		// Ocurs on every frame
	}
	public void onWillRender() {
		// When ready to render
	}
	public void onGUI() {
		// Whilst gui is renderd
	}
	
	public void print(String s) { Debug.log(s); }
	public void print(int i) { Debug.log(i); }
	public void print(boolean b) { Debug.log(b); }
	public void print(float f) { Debug.log(f); }
	
	public final Entity getOwner() {
		return entity;
	}
	
	public String getName() {
		return name;
	}
}
