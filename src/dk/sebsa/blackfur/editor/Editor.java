package dk.sebsa.blackfur.editor;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUISkin;
import dk.sebsa.blackfur.gui.GUIStyle;

public class Editor {
	private static Hierarchy hierarchy;
	private static Inspector inspector;
	
	private static Entity selected;
	
	public static GUISkin skin;
	public static GUIStyle arrowDown;
	public static GUIStyle arrowRight;
	public static GUIStyle windowStyle;
	
	public static void init() {
		skin = GUI.skin;
		
		arrowDown = skin.getStyle("ArrowDown");
		arrowRight = skin.getStyle("ArrowRight");
		windowStyle = skin.getStyle("Window");
		
		hierarchy = new Hierarchy();
		inspector = new Inspector();
	}
	
	public static void render() {
		// Render gui
		GUI.Prepare();
		
		Debug.draw();
		GUI.window(new Rect(0, 0, 200, Application.getHeight() - 30), "Hierarchy", hierarchy::render, windowStyle);
		GUI.window(new Rect(Application.getWidth()-200, 0, 200, Application.getHeight() - 30), "Inspector", inspector::render, windowStyle);
		
		GUI.unbind();
	}

	public static final Entity getSelected() {
		return selected;
	}

	public static void setSelected(Entity selected) {
		Editor.selected = selected;
		inspector.setAttributes(selected);
	}
}
