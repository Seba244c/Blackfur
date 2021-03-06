package dk.sebsa.blackfur.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dk.sebsa.blackfur.engine.Material;
import dk.sebsa.blackfur.engine.Rect;
import java.io.FileReader;
import java.io.File;

public class Sprite {
	public String name;
	public Material material;
	public Rect offset;
	public Rect padding;
	
	private static List<Sprite> sprites = new ArrayList<Sprite>();
	private static int i;
	
	private File f = null;
	private long lastModified;
	
	public Sprite(String n, Material m, Rect offset, Rect padding) {
		name = n;
		material = m;
		this.offset = offset;
		this.padding = padding;
		sprites.add(this);
	}
	
	@SuppressWarnings("resource")
	public Sprite(String name) {
		BufferedReader br;
		
		try {
			if(name.startsWith("/")) {
				InputStreamReader isr =  new InputStreamReader(Sprite.class.getResourceAsStream("/Sprites" + name + ".bfs"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				f = new File(name + ".bfs"); lastModified = f.lastModified();
				
				br = new BufferedReader(new FileReader(f));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
			// Get material
			material = Material.getMat(br.readLine().split(" ")[1]);	
				
			// Get offset
			String[] o = br.readLine().split(" ")[1].split(",");
			offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
			
			// Get padding
			String[] p = br.readLine().split(" ")[1].split(",");
			padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));

			br.close();
			
			// Add to list
			sprites.add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Rect getUV() {
		if(offset == null) return null;
		
		float w = material.texture.getWidth();
		float h = material.texture.getHeight();
		return new Rect(offset.x / w, offset.y / h, offset.width / w, offset.height / h);
	}

	public static Sprite getSprite(String name) {
		for(i = 0; i < sprites.size(); i++ ) {
			if(sprites.get(i).name.equals(name)) return sprites.get(i);
		}
		return null;
	}

	public static List<Sprite> getSprites() {
		return sprites;
	}
	
	public static void refreshAll() {
		for(i = 0; i < sprites.size(); i++) {
			try {
				sprites.get(i).refresh();
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
		
		material = Material.getMat(br.readLine().split(" ")[1]);	
		
		// Get offset
		String[] o = br.readLine().split(" ")[1].split(",");
		offset = new Rect(Float.parseFloat(o[0]), Float.parseFloat(o[1]), Float.parseFloat(o[2]), Float.parseFloat(o[3]));
		
		// Get padding
		String[] p = br.readLine().split(" ")[1].split(",");
		padding = new Rect(Float.parseFloat(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]));

		br.close();
	}
}
