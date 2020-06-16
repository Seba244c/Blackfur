package io.github.seba244c.blackfur.editor;

import java.util.ArrayList;
import java.util.List;

import io.github.seba244c.blackfur.engine.Application;
import io.github.seba244c.blackfur.engine.Entity;
import io.github.seba244c.blackfur.engine.Rect;
import io.github.seba244c.blackfur.gui.GUI;

public class Hierarchy {
	private String selected;
	private Rect clickRect;
	private int i;
	
	public Hierarchy() {
		Entity a = new Entity("a");
		
		Entity b = new Entity("b");
		b.parent(a);
		
		new Entity("c");
		
		Entity d = new Entity("d");
		d.parent(b);
	}
	
	public void render(Rect r) {
		List<Entity> updateList = new ArrayList<Entity>();
		int offsetY = 0;
		
		updateList.add(Entity.master());
		while(updateList.size() > 0) {
			Entity entity = updateList.get(0);
			List<Entity> children = entity.getChildren();
			
			if(children.size() > 0) {
				if(entity.isExpanded()) for(i = 0; i < children.size(); i++) {
					updateList.add(0, children.get(i));
				}
			}
			if(entity == Entity.master()) {
				updateList.remove(updateList.size() - 1);
				continue;
			}
			
			float inline = (entity.getInline()) * 16;
			clickRect = new Rect(0, offsetY, r.width, 20);
			
			if(selected != null) {
				if(entity.getId().equals(selected)) GUI.box(clickRect, "Box");
			}
			
			if(children.size() > 0) {
				entity.setExpanded(GUI.toggle(entity.isExpanded(), inline, offsetY+2, Editor.arrowDown, Editor.arrowRight));
				GUI.label(entity.name, inline + 15, offsetY);
			}
			else {
				GUI.label(entity.name, inline, offsetY);
			}
			
			clickRect.set(0, r.y+offsetY, r.width, 20);
			
			if(clickRect.inRect(Application.input.getMousePosition())) {
				if(Application.input.isButtonPressed(0))
					selected = entity.getId();
			}
			
			offsetY += 20;
			updateList.remove(entity);
		}
	}
}
