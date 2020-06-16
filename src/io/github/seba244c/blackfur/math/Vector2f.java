package io.github.seba244c.blackfur.math;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public void zero() {
		this.x = 0;
		this.y = 0;
	}
}
