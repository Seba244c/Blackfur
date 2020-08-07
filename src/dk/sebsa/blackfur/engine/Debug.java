package dk.sebsa.blackfur.engine;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_messageBox;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.blackfur.gui.GUI;

public class Debug {
	private static List<String> log = new ArrayList<String>();
	private static int i = 0;
	
	public static void log(int i) { log(String.valueOf(i)); }
	public static void log(boolean b) { log(String.valueOf(b)); }
	public static void log(float f) { log(String.valueOf(f)); }
	public static void log(double d) { log(String.valueOf(d)); }
	public static void log(String message) {
		addMessage(message);
	}
	
	private static void addMessage(String message) {
		log.add(message);
		if(log.size() > 20) {
			log.remove(0);
		}
		
	}

	public static String getLog() {
		String ret = "";
		
		for(i=0; i<log.size(); i++) {
			ret += (log.get(i)) + "\n";
		}
		
		return ret;
	}
	
	public static String lastEntry() {
		if(log.size()==0) {
			return "";
		}
		return log.get(log.size()-1);
	}
	
	public static void draw() {
		if(GUI.buttonPressed(Debug.lastEntry(), new Rect(0, Application.getHeight()-30, Application.getWidth(), 30), "Button", "ButtonHover") ) {
			tinyfd_messageBox("Debug Log", Debug.getLog(), "ok", "", true);
		}
	}
}
