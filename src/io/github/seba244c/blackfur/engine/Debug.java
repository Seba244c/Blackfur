package io.github.seba244c.blackfur.engine;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_messageBox;

import java.util.ArrayList;
import java.util.List;

import io.github.seba244c.blackfur.gui.GUI;

public class Debug {
	private static List<String> log = new ArrayList<String>();
	private static int i = 0;
	
	public static void Log(String message) {
		AddMessage(message);
	}
	
	private static void AddMessage(String message) {
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
	
	public static String LastEntry() {
		if(log.size()==0) {
			return "";
		}
		return log.get(log.size()-1);
	}

	public static void Log(int i) {
		Log(String.valueOf(i));
	}
	
	public static void draw() {
		if(GUI.buttonPressed(Debug.LastEntry(), new Rect(0, Application.getHeight()-30, Application.getWidth(), 30), "Button", "ButtonHover") ) {
			tinyfd_messageBox("Debug Log", Debug.getLog(), "cancel", "", true);
		}
	}
}
