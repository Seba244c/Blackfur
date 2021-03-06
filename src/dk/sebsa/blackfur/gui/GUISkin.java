package dk.sebsa.blackfur.gui;

import java.util.List;
import java.util.regex.Pattern;

import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.engine.Texture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GUISkin {
	private String name;
	private List<GUIStyle> styles = new ArrayList<GUIStyle>();
	private static List<GUISkin> skins = new ArrayList<GUISkin>();
	public Texture texture;
	private int i;
	
	private File f = null;
	private long lastModified;
	
	@SuppressWarnings("resource")
	public GUISkin(String name) {
		BufferedReader br;
	
		try {
			if(name.startsWith("/")) {
				InputStreamReader isr =  new InputStreamReader(GUISkin.class.getResourceAsStream("/Skins" + name + ".bfo"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				f = new File(name + ".bfo"); lastModified = f.lastModified();
				br = new BufferedReader(new FileReader(f));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
			texture = Texture.findTexture(br.readLine().split(" ")[1]);
			
			String line = br.readLine();
			while(line!=null) {
				if(line.startsWith("Name:")) {
					String[] o = br.readLine().split(" ")[1].split(",");
					String[] p = br.readLine().split(" ")[1].split(",");
					
					Rect offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
					Rect padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));
					GUIStyle style = new GUIStyle(line.split(" ")[1], texture, offset, padding);
					styles.add(style);
				}
				line = br.readLine();
			}
			
			br.close();
			skins.add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GUIStyle getStyle(String name) {
		for(i = 0; i < styles.size(); i++ ) {
			if(styles.get(i).name.equals(name)) return styles.get(i);
		}
		return null;
	}
	
	public static GUISkin getSkin(String name) {
		for(int i = 0; i < skins.size(); i++ ) {
			if(skins.get(i).name.equals(name)) return skins.get(i);
		}
		return null;
	}

	public static final List<GUISkin> getSkins() {
		return skins;
	}
	
	public final String getName() { return name; }

	public static void refreshAll() {
		for(int i = 0; i < skins.size(); i++) {
			try {
				skins.get(i).refresh();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void refresh() throws IOException {
		if(f==null) return;
		File temp = new File(f.getAbsolutePath());
		if(!temp.exists()) return;
		
		if(temp.lastModified() == lastModified) return;
		f = temp;
		lastModified = f.lastModified();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		texture = Texture.findTexture(br.readLine().split(" ")[1]);
		
		String line = br.readLine();
		while(line!=null) {
			if(line.startsWith("Name:")) {
				String[] o = br.readLine().split(" ")[1].split(",");
				String[] p = br.readLine().split(" ")[1].split(",");
				
				Rect offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
				Rect padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));
				String styleName = line.split(" ")[1];
				GUIStyle style = getStyle(styleName);
				if(style != null) {
					style.offset = offset;
					style.padding = padding;
				} else {
					style = new GUIStyle(line.split(" ")[1], texture, offset, padding);
					styles.add(style);
				}
			}
			line = br.readLine();
		}
		
		br.close();
	}
}
