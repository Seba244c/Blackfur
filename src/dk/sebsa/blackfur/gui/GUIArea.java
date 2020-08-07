package dk.sebsa.blackfur.gui;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.math.Mathf;

public class GUIArea {
	public Rect area = new Rect(0, 0, 0, 0);
	public int scrollHeight;
	
	private int scroll = 0;
	
	public GUIArea(Rect area) {
		if(area != null)
			this.area = area;
	}
	
	public final int getScroll() { return scroll; }
	public final int scroll(int offset) {
		if(area.inRect(Application.input.getMousePosition())) {
			float leftOver = scrollHeight - area.height;
			
			if(leftOver <= 0) scroll = 0;
			else scroll = (int)Mathf.clamp(offset + (-Application.input.getScrollY() * 10), 0, leftOver);
		}
		else scroll = offset;
		
		return scroll;
	}
}
