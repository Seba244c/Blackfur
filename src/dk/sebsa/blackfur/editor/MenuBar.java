package dk.sebsa.blackfur.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.engine.SceneManager;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUIStyle;
import dk.sebsa.blackfur.math.Color;

public class MenuBar {
	public Map<String, List<MenuItem>> menu = new LinkedHashMap<String, List<MenuItem>>();
	
	private GUIStyle box;
	private GUIStyle empty;
	private GUIStyle play;
	private GUIStyle stop;
	
	private String selected;
	private Color prevColor;
	
	public MenuBar() {
		box = Editor.skin.getStyle("Box");
		play = Editor.skin.getStyle("PlayButton");
		stop = Editor.skin.getStyle("StopButton");
		
		add("File", new MenuItem("New Scene", this::file));
		add("File", new MenuItem("Open Scene", this::file));
		add("File", new MenuItem("Save Scene", this::file));
		
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
				if(GUI.centeredButton(m, nameRect, box, box) == 1) {
					selected = null;
					GUI.removePopup();
				}
			} else {
				if(GUI.centeredButton(m, nameRect, empty, box) == 1) {
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
		
		GUIStyle style = play;
		boolean isPlaying = Editor.isPlaying();
		if(isPlaying) style = stop;
		
		if(GUI.buttonPressed("", new Rect(offset - index + 5, 8, 16, 16), style, style)) {
			Editor.play(!isPlaying);
			
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
		if(m.name.equals("Quit")) GLFW.glfwSetWindowShouldClose(Application.getWindow(), true);
		else if(m.name.equals("Save Scene")) {
			try {
				Editor.saveScene(SceneManager.currentScene());
			} catch (IOException e) {
				Debug.log("Could not save scene: " + SceneManager.currentScene());
			}
		} else if(m.name.equals("New Scene")) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(Editor.getProjectDir() + "Scenes/"));
			fc.setFileFilter(new FileNameExtensionFilter("Blackfur World File", "bfw"));
			
			int result = fc.showSaveDialog(null);
			
			if(result == JFileChooser.APPROVE_OPTION) {
				File temp = fc.getSelectedFile();
				String name = temp.getName();
				
				if(name.endsWith(".bfw")) name = name.replace(".bfw", "");
				
				try {
					File f = new File(Editor.getProjectDir() + "Scenes/" + name + ".bfw");
					f.createNewFile();
					SceneManager.loadScene(name);
				} catch (IOException e) {
					Debug.log("Could not save new scene ");
				}
			}
		} else if(m.name.equals("Open Scene")) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(Editor.getProjectDir() + "Scenes/"));
			fc.setFileFilter(new FileNameExtensionFilter("Blackfur World File", "bfw"));
			
			int result = fc.showSaveDialog(null);
			if(result == JFileChooser.APPROVE_OPTION) {
				SceneManager.loadScene(fc.getSelectedFile().getName().split("\\.")[0]);
			}
		}
	}
}
