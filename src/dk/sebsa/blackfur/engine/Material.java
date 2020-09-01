package dk.sebsa.blackfur.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dk.sebsa.blackfur.math.Color;

public class Material {
	public String name;
	public Texture texture;
	public Color color;
	public Shader shader;
	
	private static List<Material> materials = new ArrayList<Material>();
	private static int i;
	
	private File f = null;
	private long lastModified;
	
	public Material(String n, Texture t, Color c, Shader s) {
		name = n;
		texture = t;
		color = c;
		shader = s;
		materials.add(this);
	}
	
	@SuppressWarnings("resource")
	public Material(String name) {
		BufferedReader br;
	
		try {
			if(name.startsWith("/")) {
				InputStreamReader isr =  new InputStreamReader(Material.class.getResourceAsStream("/Materials" + name + ".bfm"));
				br = new BufferedReader(isr);
				this.name = name.replaceFirst("/", "");
			} else {
				f = new File(name+".bfm"); lastModified = f.lastModified();
				br = new BufferedReader(new FileReader(f));
				String[] split = name.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
				this.name = split[split.length - 1];
			}
			
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

	public static final List<Material> getMaterials() {
		return materials;
	}
	
	public static void refreshAll() {
		for(i = 0; i < materials.size(); i++) {
			try {
				materials.get(i).refresh();
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
		
		// Get texture
		texture = Texture.findTexture(br.readLine().split(" ")[1]);	
			
		// Get color
		String[] c = br.readLine().split(" ")[1].split(",");
		color = new Color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
			
		// Get shader
		shader = Shader.find(br.readLine().split(" ")[1]);

		br.close();
	}
}
