package dk.sebsa.blackfur.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.blackfur.math.Color;

public class Material {
	public String name;
	public Texture texture;
	public Color color;
	public Shader shader;
	
	private static List<Material> materials = new ArrayList<Material>();
	private static int i;
	
	public Material(String n, Texture t, Color c, Shader s) {
		name = n;
		texture = t;
		color = c;
		shader = s;
		materials.add(this);
	}
	
	public Material(String name) {
		BufferedReader br;
	
		try {
			//br = new BufferedReader(new FileReader(new File("./res/Materials/"+name+".mat")));
			InputStreamReader isr =  new InputStreamReader(Material.class.getResourceAsStream("/Materials/" + name + ".bfm"));
			br = new BufferedReader(isr);
			
			// Get name
			this.name = name;
			
			// Get texture
			texture = Texture.findTexture(br.readLine().split(" ")[1]);	
				
			// Get color
			String[] c = br.readLine().split(" ")[1].split(",");
			color = new Color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
				
			// Get shader
			shader = Shader.find(br.readLine().split(" ")[1]);
				
			// Add to list
			materials.add(this);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Material getMat(String name) {
		for(i = 0; i < materials.size(); i++ ) {
			if(materials.get(i).name.equals(name)) return materials.get(i);
		}
		return null;
	}
	
	public void bind() {
		shader.bind();
		texture.bind();
	}
	
	public void unbind() {
		shader.unbind();
		texture.unbind();
	}
}
