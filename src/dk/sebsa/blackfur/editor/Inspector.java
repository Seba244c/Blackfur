package dk.sebsa.blackfur.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.blackfur.engine.Component;
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
		}
	}
	
	public void drawVariables(Rect r) {
		ComponentAttributes a = ca.get(i);
		
		String[] fields = ca.get(i).fields;
		for(int f = 0; f < fields.length; f++) {
			String[] split = fields[f].split(" ");
			
			if(split[1].equals("String")) {
				try {
					Field s = a.c.getDeclaredField(split[0]);
					String p = s.get(a.component).toString();
					String v = GUI.textField(new Rect(0, f * 18, r.width, 18), split[0], p);
					if(!p.equals(v))
						s.set(a.component, v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				GUI.textField(new Rect(0, f * 18, r.width, 18), split[0], "");
			}
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
