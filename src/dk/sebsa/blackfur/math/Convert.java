package dk.sebsa.blackfur.math;

import org.lwjgl.glfw.GLFW;

public class Convert {
	public static final byte[] keys = boolToByteArray(new boolean[GLFW.GLFW_KEY_LAST]);
	public static final byte[] buttons = boolToByteArray(new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST]);
	public static byte[] boolToByteArray(boolean[] b) {
		byte[] a = new byte[b.length];
		int i = 0;
		
		for(boolean bool : b) {
			a[i] = (byte) (bool?1:0);
			i++;
		}
		return a;
	}
}
