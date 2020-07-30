package dk.sebsa.blackfur.gui;

import java.util.List;
import java.util.function.Consumer;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.input.Input;
import dk.sebsa.blackfur.math.Color;
import dk.sebsa.blackfur.math.Vector2f;

public class Popup {
	private Rect nameArea;
	private List<String> list;
	private Consumer<String> func;
	private int i;
	private Rect listArea;
	private GUIStyle box;
	private Color prevColor;
	
	public Popup(Rect nameArea, List<String> list, Consumer<String> func) {
		this.nameArea = nameArea;
		this.list = list;
		
		// Get widest string inside of list
		float width = 0;
		for(i = 0; i < list.size(); i ++) {
			float w = GUI.font.getStringWidth(list.get(i));
			if(w > width) width = w;
		}
		listArea = new Rect(nameArea.x, nameArea.y + nameArea.height - 1, width + 12, list.size() * 28 + 4);
		
		box = GUI.skin.getStyle("Box");
		this.func = func;
	}
	
	public final Rect getListArea() { return listArea; }
	
	public Popup draw() {
		Vector2f mousePos = Application.input.getMousePosition();
		if(!listArea.inRect(mousePos) && !nameArea.inRect(mousePos)) return null;
		Rect drawArea = GUI.box(listArea, box);
		
		prevColor = GUI.textColor;
		GUI.textColor = Color.white();
		
		for(i = 0; i < list.size(); i++) {
			if(GUI.buttonReleased(list.get(i), new Rect(drawArea.x, drawArea.y + (i * 28), drawArea.width, 28), null, box)) {
				func.accept(list.get(i));
				GUI.textColor = prevColor;
				return null;
			}
		}
		
		GUI.textColor = prevColor;
		return this;
	}
}
