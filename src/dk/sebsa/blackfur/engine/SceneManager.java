package dk.sebsa.blackfur.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import dk.sebsa.blackfur.editor.Editor;
import dk.sebsa.blackfur.gui.Sprite;
import dk.sebsa.blackfur.math.Vector2f;

public class SceneManager {
	public static void loadScene(String name)  {
		FileReader fr;
		try { fr = new FileReader(Editor.getProjectDir() + "/Scenes/" + name + ".bfw"); } catch (FileNotFoundException e) {
			Debug.log("Scene " + name + " couldnt be found!");
			return;
		}
		
		Entity.clear();
		BufferedReader br = new BufferedReader(fr);
		String line;
		
		
		try {
			Entity e = null;
			Component c = null;
			
			while((line = br.readLine()) != null) {
				line = line.replace("\t", "");
				String[] split = line.split("\"");
				
				if(line.startsWith("<e")) e = createEntity(split);
				else if(line.startsWith("<c")) c = e.addComponent(split[1]);
				else if(line.startsWith("<v")) {
					String[] sep = line.split("<v ")[1].split("=");
					try {
						setVar(c, sep[0], sep[1].split("\"")[1]);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
							| IllegalAccessException e1) {
						Debug.log("Could not set " + sep[0]);
					}
					
				}
				else if(line.startsWith("<p")) e.parent(Entity.find(split[1]));
			}
			
			br.close();
			fr.close();
		} catch (IOException e1) { e1.printStackTrace(); }	
	}

	private static void setVar(Component c, String field, String v) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = c.getClass().getField(field);
		if(f == null) return;
		
		String[] splitName = f.getType().toString().split("\\.");
		String dataType = splitName[splitName.length - 1];

		if(dataType.equals("String")) { f.set(c, v); return; }
		else if(dataType.equals("int")) { f.set(c, Integer.parseInt(v)); return; }
		else if(dataType.equals("Vector2f")) { String[] split = v.split(" "); f.set(c, new Vector2f(Float.parseFloat(split[0]), Float.parseFloat(split[1]))); return; }
		else if(dataType.equals("Sprite")) {  f.set(c, Sprite.getSprite(v)); return; }
		else if(dataType.equals("boolean")) { f.set(c, Boolean.parseBoolean(v)); return; }
		else if(dataType.equals("float")) { f.set(c, Float.parseFloat(v)); return; }
	}

	private static Entity createEntity(String[] split) {
		Entity e = new Entity(split[1]);
		
		String[] tSplit = split[3].split(" ");
		e.setPosition(Float.parseFloat(tSplit[0]), Float.parseFloat(tSplit[1]));
		
		tSplit = split[5].split(" ");
		e.setPosition(Float.parseFloat(tSplit[0]), Float.parseFloat(tSplit[1]));
		
		e.setRotation(Float.parseFloat(split[7]));
		
		return e;
	}
}
