package io.github.seba244c.blackfur.editor;

import io.github.seba244c.blackfur.engine.Application;
import io.github.seba244c.blackfur.engine.Debug;
import io.github.seba244c.blackfur.engine.Rect;
import io.github.seba244c.blackfur.gui.GUI;
import io.github.seba244c.blackfur.gui.GUISkin;
import io.github.seba244c.blackfur.gui.GUIStyle;

public class Editor {
	private static Hierarchy hierarchy;
	public static GUISkin skin;
	public static GUIStyle arrowDown;
	public static GUIStyle arrowRight;
	
	public static void init() {
		skin = GUI.skin;
		
		arrowDown = skin.getStyle("ArrowDown");
		arrowRight = skin.getStyle("ArrowRight");
		
		hierarchy = new Hierarchy();
	}
	
	public static void render() {
		// Render gui
		GUI.Prepare();
		
		Debug.draw();
		GUI.window(new Rect(0, 0, 200, Application.getHeight() - 30), "Hierarchy", hierarchy::render, "Window");
		
		GUI.unbind();
	}
}
