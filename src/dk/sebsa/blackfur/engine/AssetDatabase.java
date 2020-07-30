package dk.sebsa.blackfur.engine;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dk.sebsa.blackfur.gui.Font;
import dk.sebsa.blackfur.gui.GUISkin;
import dk.sebsa.blackfur.gui.Sprite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;

public class AssetDatabase {
	private static Class<AssetDatabase> clazz = AssetDatabase.class;
	private static ClassLoader cl = clazz.getClassLoader();
	private static List<String> textures = new ArrayList<String>();
	private static List<String> fonts = new ArrayList<String>();
	private static List<String> shaders = new ArrayList<String>();
	private static List<String> materials = new ArrayList<String>();
	private static List<String> sprites = new ArrayList<String>();
	private static List<String> skins = new ArrayList<String>();
	
	public static void loadAllResources() throws IOException {
		int i = 0;
		initResourcePaths();
		
		for(i = 0; i < textures.size(); i++) new Texture(textures.get(i));
		for(i = 0; i < fonts.size(); i++) new Font(fonts.get(i), 16);
		for(i = 0; i < shaders.size(); i++) new Shader(shaders.get(i));
		for(i = 0; i < materials.size(); i++) new Material(materials.get(i));
		for(i = 0; i < sprites.size(); i++) new Sprite(sprites.get(i));
		for(i = 0; i < skins.size(); i++) new GUISkin(skins.get(i));
	}

	private static void initResourcePaths() {
		URL dirUrl = cl.getResource("dk/sebsa/blackfur/engine");
		String protocol = dirUrl.getProtocol();
		
		try {
			if(dirUrl != null && protocol.equals("file")) importFromDir();
			else importFromJar(dirUrl);
		} catch (IOException e) { System.out.println("Error loading assets:"); e.printStackTrace(); }
		
	}

	private static void importFromJar(URL dirUrl) throws UnsupportedEncodingException, IOException {
		// Loads the engine resources from a jar
		String me = clazz.getName().replace(".", "/") + ".class";
		dirUrl = cl.getResource(me);
		
		if(dirUrl.getProtocol().equals("jar")) {
			String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entires = jar.entries();
			
			while(entires.hasMoreElements()) {
				String name = entires.nextElement().getName();
				if(name.endsWith("/")) continue;
				else if(name.startsWith("Textures")) { textures.add(name.split("/")[1]); }
				else if(name.startsWith("Font")) { fonts.add(name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("Shaders")) { shaders.add(name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("Materials")) { materials.add(name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("Sprites")) { sprites.add(name.split("/")[1].split("\\.")[0]); }
				else if(name.startsWith("Skins")) { skins.add(name.split("/")[1].split("\\.")[0]); }
			}
			jar.close();
		}
	}

	private static void importFromDir() throws IOException {
		// Loads engine resources from folders
		textures = importFromLocalDir("Textures", 1);
		fonts = importFromLocalDir("Font", 0);
		shaders = importFromLocalDir("Shaders", 0);
		materials = importFromLocalDir("Materials", 0);
		sprites = importFromLocalDir("Sprites", 0);
		skins = importFromLocalDir("Skins", 0);
	}

	private static List<String> importFromLocalDir(String path, int useExt) throws IOException {
		List<String> paths = new ArrayList<String>();
		InputStream in = cl.getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		
		while((line = br.readLine()) != null) {
			if(useExt == 1)
				paths.add(line);
			else
				paths.add(line.split("\\.")[0]);
		}
		
		in.close();
		br.close();
		return paths;
	}
}
