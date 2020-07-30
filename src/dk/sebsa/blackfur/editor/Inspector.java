package dk.sebsa.blackfur.editor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import dk.sebsa.blackfur.engine.Component;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUIStyle;
import dk.sebsa.blackfur.math.Vector2f;

public class Inspector {
	private List<ComponentAttributes> ca = new ArrayList<ComponentAttributes>();
	private int i = 0;
	private int offsetY = 0;
	public GUIStyle windowStyle;
	
	public Inspector() {
		windowStyle = Editor.skin.getStyle("Window");
	}
	
	public void render(Rect r) {
		Object inspected = Editor.getInspected();
		if(inspected == null) return;
		
		if(inspected instanceof Entity) {
			// Get Selected
			Entity selected = (Entity) inspected;
			//if(selected == null) return;
			// Render transform info and name
			selected.name = GUI.textField(new Rect(0, 0, r.width, 22), "Entity", selected.name, 100);
			selected.setPosition(GUI.vectorField(new Rect(0, 24, r.width, 22), "Position", selected.getPosition(), 100));
			selected.setScale(GUI.vectorField(new Rect(0, 48, r.width, 22), "Scale", selected.getScale(), 100));
			selected.setRotation(GUI.floatField(new Rect(0, 72, r.width, 22), "Rotation", selected.getRotation(), 100));
			int offsetY = 96;
			
			// Render components
			for(i = 0; i < ca.size(); i++) {
				ComponentAttributes att = ca.get(i);
				
				float h = att.height + (windowStyle.padding.y + windowStyle.padding.height);
				GUI.window(new Rect(0, offsetY, r.width, h), ((Component) att.component).getName(), this::drawVariables, windowStyle);
				offsetY += h + 2;
			}
		
			// Add component button
			if(GUI.buttonReleased("+ Add Component +", new Rect(0, offsetY, r.width, 26), "Button", "ButtonHover")) {
				String output = TinyFileDialogs.tinyfd_inputBox("Add Component", "What component would you like to add?", "");
				
				if(output == null) return;
				
				Class<?> cls;
				try {
					cls = Class.forName("dk.sebsa.blackfur.game." + output);
					
					try {
						selected.addComponent((Component) cls.getConstructor().newInstance());
						setAttributes(selected);
					} catch (InstantiationException | IllegalAccessException e) {
						TinyFileDialogs.tinyfd_messageBox("Could not add component!", "The component you are trying to add does not exist in the game package", "ok", "Error", true);
					}
					catch (IllegalArgumentException e) { e.printStackTrace();
					} catch (InvocationTargetException e) { e.printStackTrace();
					} catch (NoSuchMethodException e) { e.printStackTrace();
					} catch (SecurityException e) { e.printStackTrace(); }
				} catch (ClassNotFoundException e) {
					TinyFileDialogs.tinyfd_messageBox("Could not add component!", "The component you are trying to add does not exist in the game package", "ok", "Error", true);
				}
			}
		}
		else {
			i = 0;
			GUI.window(new Rect(0, offsetY, r.width, ca.get(0).height + (windowStyle.padding.y + windowStyle.padding.height)), ca.get(0).component.getClass().getSimpleName(), this::drawVariables, windowStyle);
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
					try {
						String p = s.get(a.component).toString();
						String v = GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p, 100);
						if(!p.equals(v))
							s.set(a.component, v);
					}
					catch(IllegalArgumentException e) { e.printStackTrace(); }
					catch(IllegalAccessException e) { e.printStackTrace(); }
				}
				catch (NoSuchFieldException e) { e.printStackTrace(); }
				catch (SecurityException e) { e.printStackTrace(); }
			}
			else if(split[1].equals("float")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					try {
						String p = s.get(a.component).toString();
						
						float fl = Float.parseFloat(p);
						float v = GUI.floatField(new Rect(0, f * 22 +padding, r.width, 22), split[0], fl, 100);
						if(!p.equals(String.valueOf(v)))
							s.set(a.component, v);
						
					}
					catch(IllegalArgumentException e) { e.printStackTrace(); }
					catch(IllegalAccessException e) { e.printStackTrace(); }
				}
				catch (NoSuchFieldException e) { e.printStackTrace(); }
				catch (SecurityException e) { e.printStackTrace(); }
			}
			else if(split[1].equals("int")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					try {
						String p = s.get(a.component).toString();
						String v = GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p, 100);
						if(!p.equals(v)) {
							try { s.set(a.component, Integer.parseInt(v)); }
							catch (NumberFormatException e) {
								Debug.log("Int field input is inviliad! ue#0001");
							}
						}	
					}
					catch(IllegalArgumentException e) { e.printStackTrace(); }
					catch(IllegalAccessException e) { e.printStackTrace(); }
				}
				catch (NoSuchFieldException e) { e.printStackTrace(); }
				catch (SecurityException e) { e.printStackTrace(); }
			}
			else if(split[1].equals("Vector2f")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					try {
						Vector2f p = (Vector2f) s.get(a.component);
						Vector2f v = GUI.vectorField(new Rect(0, f * 22 + padding, r.width, 22), split[0], p, 100);
						if(!p.equals(v)) {
							s.set(a.component, v);
						}	
					}
					catch(IllegalArgumentException e) { e.printStackTrace(); }
					catch(IllegalAccessException e) { e.printStackTrace(); }
				}
				catch (NoSuchFieldException e) { e.printStackTrace(); }
				catch (SecurityException e) { e.printStackTrace(); }
			}
			else { GUI.textField(new Rect(0, f * 22 + padding, r.width, 22), split[0], "", 100); }
			padding += 2;
		}
	}
	
	public void setAttributes(Object o) {
		ca.clear();
		if(o instanceof Entity) {
			List<Component> c = ((Entity) o).getComponents();
			
			for(i = 0; i < c.size(); i++) {
				ComponentAttributes a = new ComponentAttributes(o);
				if(a != null) ca.add(new ComponentAttributes(c.get(i)));
			}
			return;
		}
		ComponentAttributes a = new ComponentAttributes(o);
		if(a != null) ca.add(a);
	}
}
