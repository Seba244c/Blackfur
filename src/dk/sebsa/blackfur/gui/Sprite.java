package dk.sebsa.blackfur.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.blackfur.engine.Material;
import dk.sebsa.blackfur.engine.Rect;

public class Sprite {
	public String name;
	public Material material;
	public Rect offset;
	public Rect padding;
	
	private static List<Sprite> sprites = new ArrayList<Sprite>();
	private static int i;
	
	public Sprite(String n, Material m, Rect offset, Rect padding) {
		name = n;
		material = m;
		this.offset = offset;
		this.padding = padding;
		sprites.add(this);
	}
	
	public Sprite(String name) {
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(new File("./res/Sprites/"+name+".spr")));
			
			// Get name
			this.name = name;
			
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

	public static Sprite getSprite(String name) {
		for(i = 0; i < sprites.size(); i++ ) {
			if(sprites.get(i).name.equals(name)) return sprites.get(i);
		}
		return null;
	}
}
