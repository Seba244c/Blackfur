package dk.sebsa.blackfur.gui;

import dk.sebsa.blackfur.math.Vector2f;

public class Glyph {
	public Vector2f position;
	public Vector2f size;
	public Vector2f scale;
	
	public Glyph(Vector2f position, Vector2f size, Vector2f scale) {
		this.position = position;
		this.size = size;
		this.scale = scale;
	}
}
