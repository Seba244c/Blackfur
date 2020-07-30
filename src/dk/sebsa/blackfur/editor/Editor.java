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
	private static ProjectPanel projectPanel;
	private static MenuBar menuBar;
	
	private static Entity selected;
	private static Object selectedAsset;
	private static Object inspected;
	
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
		projectPanel = new ProjectPanel();
		menuBar = new MenuBar();
	}
	
	public static void render() {
		// Render gui
		GUI.prepare();
		
		Debug.draw();
		menuBar.render();
		GUI.window(new Rect(0, 30, 400, Application.getHeight() - 60), "Hierarchy", hierarchy::render, windowStyle);
		GUI.window(new Rect(Application.getWidth()-400, 30, 400, Application.getHeight() - 60), "Inspector", inspector::render, windowStyle);
		GUI.window(new Rect(0, Application.getHeight() - 230, 400, 200), "Asset Types", projectPanel::renderTypes, windowStyle);
		GUI.window(new Rect(400, Application.getHeight() - 230, Application.getWidth() - 400, 200), "Assets", projectPanel::renderAssets, windowStyle);
		
		GUI.unbind();
	}

	public static final Entity getSelected() {
		return selected;
	}

	public static void setSelected(Entity selected) {
		Editor.selected = selected;
		setInspected((Object) selected);
	}
	
	public static final Object getSelectedAsset() {return selectedAsset;}
	public static void setSelectedAsset(Object o)
	{
		selectedAsset = o;
		setInspected(o);
	}
	
	public static final Object getInspected() {return inspected;}
	private static void setInspected(Object o){inspected = o; inspector.setAttributes(o);}

	public static final Hierarchy getHierarchy() {
		return hierarchy;
	}

	public static final Inspector getInspector() {
		return inspector;
	}

	public static final ProjectPanel getProjectPanel() {
		return projectPanel;
	}
}
