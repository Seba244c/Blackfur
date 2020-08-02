package dk.sebsa.blackfur.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

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
	
	public static final String workspaceDir = System.getProperty("user.dir") + "/Blackfur-Workspace/";
	public static final String editorVersion = "0.0.1 Snapshot";
	public static byte configInit = 0;
	private static String projectDir = "";
	
	public static void initConfig() {
		if(configInit == 1) return;
		configInit = 1;
		
		File configFile = new File(workspaceDir + "config.properties");
		
		try {
			FileReader fr = new FileReader(configFile);
			Properties p = new Properties();
			p.load(fr);
			
			String recordVersion = p.getProperty("version");
			
			if(!editorVersion.equalsIgnoreCase(recordVersion)) {
				System.out.println("Version changed!");
			} else System.out.println("Same version");
			
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("New User");
			saveConfig(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	public static void openProject(String name) {
		projectDir = workspaceDir + name + "/";
		
		File dir = new File(projectDir);
		Boolean newPro = dir.mkdir();
		
		new File(projectDir + "Font/").mkdir();
		new File(projectDir + "Materials/").mkdir();
		new File(projectDir + "Shaders/").mkdir();
		new File(projectDir + "Skins/").mkdir();
		new File(projectDir + "Sprites/").mkdir();
		new File(projectDir + "Textures/").mkdir();
		
		if(newPro) return;
		
		// Put openening if project here
	}
	
	private static void saveConfig(File configFile) {
		try {
			File dir = new File(workspaceDir);
			dir.mkdir();
			
			Properties p = new Properties();
			p.setProperty("version", editorVersion);
			
			FileWriter writer = new FileWriter(configFile);
			p.store(writer, "Blackfur Configuration");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
