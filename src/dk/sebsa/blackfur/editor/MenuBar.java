package dk.sebsa.blackfur.editor;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUIStyle;
import dk.sebsa.blackfur.math.Color;

public class MenuBar {
	public Map<String, List<MenuItem>> menu = new LinkedHashMap<String, List<MenuItem>>();
	
	private GUIStyle box;
	private GUIStyle empty;
	private String selected;
	private Color prevColor;
	
	public MenuBar() {
		box = Editor.skin.getStyle("Box");
		add("File", new MenuItem("Quit", this::file));
		add("Asset", new MenuItem("New Entity", this::asset));
	}
	
	public void render() {
		float w = Application.getWidth();
		if(selected != null) {
			if(GUI.hasPopup() == false) selected = null;
		}
		GUI.box(new Rect(0, 0, w, 30), box);
		
		prevColor = GUI.textColor;
		GUI.textColor = Color.white();
		
		float offset = 0;
		float index = 0;
		
		for(String m : menu.keySet()) {
			float width = GUI.font.getStringWidth(m) + 10;
			Rect nameRect = new Rect(offset-index, 0, width, 30);
			if(selected != null && selected.equals(m)) {
				GUI.buttonReleased(m, nameRect, box, box);
			} else {
				if(GUI.buttonPressed(m, nameRect, empty, box)) {
					List<MenuItem> list = menu.get(m);
					List<String> v = new ArrayList<String>();
					for(int i = 0; i < list.size(); i++) v.add(list.get(i).name);
					GUI.setPopup(nameRect, v, this::clicked);
					selected = m;
				}
			}
			offset += width;
			index++;
		}
		
		GUI.textColor = prevColor;
	}
	
	public void clicked(String v) {
		List<MenuItem> list = menu.get(selected);
		for(MenuItem item : list)
			if(item.name.equals(v)) {
				item.accept();
				return;
			}
	}
	
	public void add(String parent, MenuItem item) {
		List<MenuItem> menuItems = menu.get(parent);
		if(menuItems == null) {
			menuItems = new ArrayList<MenuItem>();
			menu.put(parent, menuItems);
		}
		menuItems.add(item);
	}
	
	public void asset(MenuItem m) {
		if(m.name.equals("New Entity"))
			new Entity(true);
	}
	
	public void file(MenuItem m) {
		if(m.name.equals("Quit")) GLFW.glfwSetWindowShouldClose(Application.getWindow(), true);;
	}
}
