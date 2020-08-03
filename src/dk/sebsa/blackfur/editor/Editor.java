package dk.sebsa.blackfur.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Spring;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Component;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.Sprite;
import dk.sebsa.blackfur.gui.GUISkin;
import dk.sebsa.blackfur.gui.GUIStyle;
import dk.sebsa.blackfur.math.Vector2f;

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
			
			// I ADDED THIS
			// !!! MAYBE REMOVE !!!
			saveConfig(configFile);
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
		Application.name += " -=" + name + "=-";
		projectDir = workspaceDir + name + "/";
		
		File dir = new File(projectDir);
		Boolean newPro = dir.mkdir();
		
		new File(projectDir + "Font/").mkdir();
		new File(projectDir + "Materials/").mkdir();
		new File(projectDir + "Shaders/").mkdir();
		new File(projectDir + "Skins/").mkdir();
		new File(projectDir + "Sprites/").mkdir();
		new File(projectDir + "Textures/").mkdir();
		new File(projectDir + "Scenes/").mkdir();
		
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
	
	public static void saveScene(String name) throws IOException {
		File f = new File(projectDir + "Scenes/" + name + ".bfw");
		FileWriter fw = new FileWriter(f);
		
		// Get entities
		List<Entity> entities = new ArrayList<Entity>();
		
		Entity master = Entity.master();
		entities.add(master);
		
		while(entities.size() > 0) {
			Entity e = entities.get(0);
			List<Entity> children = e.getChildren();
			
			if(children.size() > 0) for(int i = 0; i < children.size(); i++) {
				entities.add(0, children.get(i));
			}
			
			if(e == master) { entities.remove(entities.size()-1); continue; }
			
			writeTransform(e, fw);
			
			List<Component> c = e.getComponents();
			for(int i = 0; i < c.size(); i++) { writeComponent(c.get(i), fw); }
			if(e.getParent() != master) {
				fw.write("\t<p name=\"" + e.getParent().name + "\">\n</e>\n");
			} else fw.write("</e>\n");
			entities.remove(e);
		}
		fw.close();
	}
	
	private static void writeComponent(Component c, FileWriter fw) throws IOException {
		Class<?> cls = c.getClass();
		String line = "\t<c name=\"" + cls.getCanonicalName() + "\">\n";
		fw.write(line);
		
		Field[] fields = cls.getFields();
		for(Field field : fields) {
			line = "\t\t<v " + field.getName() + "=\"";
			
			try {
				String[] t = field.getType().toString().split("\\.");
				if(t[t.length - 1].equals("Vector2f")) {
					Vector2f v = (Vector2f) field.get(c);
					line += v.x + " " + v.y;
				} else if(t[t.length - 1].equals("Sprite")) {
					line += ((Sprite) field.get(c)).name;
				} else line += field.get(c);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			fw.write(line + "\">\n");
		}
	}

	private static void writeTransform(Entity e, FileWriter fw) throws IOException {
		String line = "<e name=\"" + e.name + "\" ";
		line += "pos=\"" + e.getPosition().x + " " + e.getPosition().y + "\" ";
		line += "scale=\"" + e.getScale().x + " " + e.getScale().y + "\" ";
		line += "r=\"" + e.getRotation() + "\">\n";
		fw.write(line);
	}

	public static final String getProjectDir() {
		return projectDir;
	}
}
