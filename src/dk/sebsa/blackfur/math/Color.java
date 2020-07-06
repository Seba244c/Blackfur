package dk.sebsa.blackfur.math;

public class Color {
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	
	public Color(float r, float g, float b, float a) {
		this.r = Mathf.clamp(r, 0, 1);
		this.g = Mathf.clamp(g, 0, 1);
		this.b = Mathf.clamp(b, 0, 1);
		this.a = Mathf.clamp(a, 0, 1);
	}
	public Color(float r, float g, float b) {
		this.r = Mathf.clamp(r, 0, 1);
		this.g = Mathf.clamp(g, 0, 1);
		this.b = Mathf.clamp(b, 0, 1);
	}
	
	public static Color black() {return new Color(0, 0, 0);}
	public static Color white() {return new Color(1, 1, 1);}
	public static Color red() {return new Color(1, 0, 0);}
	public static Color green() {return new Color(0, 1, 0);}
	public static Color blue() {return new Color(0, 0, 1);}
	public static Color grey() {return new Color(0.5f, 0.5f, 0.5f);}
	public static Color wine() {return new Color(0.5f, 0, 0);}
	public static Color forest() {return new Color(0, 0.5f, 0);}
	public static Color marine() {return new Color(0, 0, 0.5f);}
	public static Color yellow() {return new Color(1, 1, 0);}
	public static Color cyan() {return new Color(1, 0, 1);}
	public static Color magenta() {return new Color(0, 1, 1);}
	public static Color transparent() {return new Color(0, 0, 0, 0);}
	
	public String toString() {
		return "("+String.valueOf(r)+", "+String.valueOf(g)+", "+String.valueOf(b)+", "+String.valueOf(a)+")";
	}
	
	public boolean Compare(Color c) {
		return c.r == r && c.g == g && c.b == b && c.a == a;
	}
}
