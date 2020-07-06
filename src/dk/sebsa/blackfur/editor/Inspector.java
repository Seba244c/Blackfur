package dk.sebsa.blackfur.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import dk.sebsa.blackfur.engine.Component;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUIStyle;

public class Inspector {
	private List<ComponentAttributes> ca = new ArrayList<ComponentAttributes>();
	private int i = 0;
	private int offsetY = 0;
	public GUIStyle windowStyle;
	
	public Inspector() {
		windowStyle = Editor.skin.getStyle("Window");
	}
	
	public void render(Rect r) {
		Entity selected = Editor.getSelected();
		if(selected == null) return;
		
		offsetY = 0;
		for(i = 0; i < ca.size(); i++) {
			ComponentAttributes att = ca.get(i);
			
			float h = att.height + (windowStyle.padding.y + windowStyle.padding.height);
			GUI.window(new Rect(0, offsetY, r.width, h), att.component.getName(), this::drawVariables, windowStyle);
			offsetY += h + 2;
		}
		
		if(GUI.buttonReleased("      + Add Component + ", new Rect(0, offsetY, r.width, 26), "Button", "ButtonHover")) {
			String output = TinyFileDialogs.tinyfd_inputBox("Add Component", "What component would you like to add?", "");
			
			if(output == null) return;
			
			Class<?> cls;
			try {
				cls = Class.forName("dk.sebsa.blackfur.game." + output);
				
				try {
					selected.addComponent((Component) cls.newInstance());
					setAttributes(selected);
				} catch (InstantiationException | IllegalAccessException e) {
					TinyFileDialogs.tinyfd_messageBox("Could not add component!", "The component you are trying to add does not exist in the game package", "ok", "Error", true);
				}
			} catch (ClassNotFoundException e) {
				TinyFileDialogs.tinyfd_messageBox("Could not add component!", "The component you are trying to add does not exist in the game package", "ok", "Error", true);
			}
		}
	}
	
	public void drawVariables(Rect r) {
		ComponentAttributes a = ca.get(i);
		
		String[] fields = ca.get(i).fields;
		int padding = 0;
		for(int f = 0; f < fields.length; f++) {
			String[] split = fields[f].split(" ");
			
			if(split[1].equals("String")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					String p = s.get(a.component).toString();
					String v = GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p);
					if(!p.equals(v))
						s.set(a.component, v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(split[1].equals("float")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					String p = s.get(a.component).toString();
					String v = GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p);
					if(!p.equals(v)) {
						try { s.set(a.component, Float.parseFloat(v)); }
						catch (NumberFormatException e) {
							Debug.Log("Float field input is inviliad! ue#0001");
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(split[1].equals("int")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					String p = s.get(a.component).toString();
					String v = GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p);
					if(!p.equals(v)) {
						try { s.set(a.component, Integer.parseInt(v)); }
						catch (NumberFormatException e) {
							Debug.Log("Int field input is inviliad! ue#0001");
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else { GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], ""); }
			padding += 2;
		}
	}
	
	public void setAttributes(Entity entity) {
		ca.clear();
		List<Component> c = entity.getComponents();
		
		for(i = 0; i < c.size(); i++) {
			ca.add(new ComponentAttributes(c.get(i)));
		}
	}
}
