package dk.sebsa.blackfur.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import dk.sebsa.blackfur.engine.Component;

public class EditorUtil {
	private static List<Component> importedComponents = new ArrayList<>();
	private static int i;
	
	@SuppressWarnings("resource")
	public static void importCompoenent(String path) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		if(stream == null) return;
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String seperator = System.getProperty("line.separator");
		String tempProperty = System.getProperty("java.io.tmpdir");
		
		String[] name = path.replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
		Path srcPath = Paths.get(tempProperty, name[name.length - 1]);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			Files.write(srcPath, reader.lines().collect(Collectors.joining(seperator)).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) { e.printStackTrace(); }
		compiler.run(null, null, null, srcPath.toString());
		Path p = srcPath.getParent().resolve(name[name.length -1].split("\\.")[0]);
		
		URL classUrl = null;
		try {
			classUrl = p.getParent().toFile().toURI().toURL();
		} catch (MalformedURLException e) { e.printStackTrace(); }
		if(classUrl == null) return;
		
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {classUrl});
		Class<?> myClass = null;
		try {
			myClass = classLoader.loadClass(name[name.length -1].split("\\.")[0]);
		} catch (ClassNotFoundException e) { e.printStackTrace(); }
		if(myClass == null) return;
		
		Component c = null;
		try {
			c = (Component) myClass.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		if(c == null) return;
		
		importedComponents.add(c);
		
		try {
			stream.close();
			reader.close();
			classLoader.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public static Component getComponent(String name) {
		for(i = 0; i < importedComponents.size(); i++) {
			if(importedComponents.get(i).getName().equals(name))
				try {
					return importedComponents.get(i).getClass().getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	public static void cleanUp() {
		String path;
		File javaFile;
		File classFile;
		
		for(i = 0; i < importedComponents.size(); i++) {
			Component c = importedComponents.get(i);
			path = c.getClass().getProtectionDomain().getCodeSource().getLocation() + c.getName();
			javaFile = new File(path + ".java");
			classFile = new File(path + ".class");
			
			javaFile.delete();
			classFile.deleteOnExit();
		}
	}
}
