package dk.sebsa.blackfur.editor;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Entity;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.gui.GUI;

public class Hierarchy {
	private int i;
	private int scroll = 0;
	
	public void render(Rect r) {
		// Calc scroll
		List<Entity> loop = new ArrayList<Entity>();
		int offset = 0;
		
		loop.add(Entity.master());
		while(loop.size() > 0) {
			Entity e = loop.get(0);
			List<Entity> c = e.getChildren();
			
			if(loop.size() > 0) {
				if(e.isExpanded()) for(i = 0; i < c.size(); i++) {
					loop.add(0, c.get(i));
				}
			}
			if(e == Entity.master()) {
				loop.remove(loop.size()-1);
				continue;
			}
			offset += 20;
			loop.remove(e);
		}
		scroll = GUI.setScrollView(offset, scroll);
		
		// Render
		List<Entity> updateList = new ArrayList<Entity>();
		int offsetY = 0;
		
		updateList.add(Entity.master());
		while(updateList.size() > 0) {
			Entity entity = updateList.get(0);
			List<Entity> children = entity.getChildren();
			
			if(children.size() > 0 && entity.isExpanded()) {
				for(i = 0; i < children.size(); i++) {
					updateList.add(0, children.get(i));
				}
			}
			if(entity == Entity.master()) {
				updateList.remove(updateList.size() - 1);
				continue;
			}
			
			float inline = (entity.getInline()) * 16;
			Rect clickRect = new Rect(0, offsetY, r.width, 20);
			
			Entity selected = Editor.getSelected();
			if(selected != null && entity == selected) {
				GUI.box(clickRect, "Box");
			}
			
			if(children.size() > 0) {
				entity.setExpanded(GUI.toggle(entity.isExpanded(), inline, offsetY+2, Editor.arrowDown, Editor.arrowRight));
				GUI.label(entity.name, inline + 15, offsetY);
			}
			else {
				GUI.label(entity.name, inline, offsetY);
			}
			
			clickRect.set(0, (r.y+offsetY) - scroll, r.width, 20);
			
			if(clickRect.inRect(Application.input.getMousePosition()) && Application.input.isButtonPressed(0)) {
				Editor.setSelected(entity);
			}
			
			offsetY += 20;
			updateList.remove(entity);
		}
	}
}
