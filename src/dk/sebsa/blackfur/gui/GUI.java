package dk.sebsa.blackfur.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Debug;
import dk.sebsa.blackfur.engine.Mesh;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.engine.Shader;
import dk.sebsa.blackfur.engine.Texture;
import dk.sebsa.blackfur.math.Color;
import dk.sebsa.blackfur.math.Matrix4x4;
import dk.sebsa.blackfur.math.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_inputBox;

public class GUI {
	public static Color tintColor = Color.white();
	public static Color textColor = Color.white();
	
	private static Matrix4x4 ortho;
	private static Mesh mesh;
	private static Shader shader;
	private static Popup popup;
	
	private static int i;
	private static char[] c;
	public static Font font;
	private static float tempX;
	public static GUISkin skin;
	
	private static List<GUIArea> areas = new ArrayList<GUIArea>();
	private static int area = 0;
	
	public static void init() throws IOException {
		skin = GUISkin.getSkin("DefaultGUI");
		
		float[] square = new float[] {
			0, 1, 1, 1, 1, 0,
			1, 0, 0, 0, 0, 1
		};
		mesh = new Mesh(square, square);
		shader = Shader.find("DefaultEngineShader");
		font = new Font(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 16));
	}
	
	public static void prepare() {
		// Clear ares
		areas.clear();
		areas.add(new GUIArea(Application.getRect()));
		area = 0;
		
		// Disable 3d
		glDisable(GL_DEPTH_TEST);
		
		// Render preparation
		shader.bind();
		ortho = Matrix4x4.ortho(0, Application.getWidth(), Application.getHeight(), 0, -1, 1);
		shader.setUniform("projection", ortho);
		mesh.bind();
		
		
	}
	
	public static boolean hasPopup() {
		return popup != null;
	}
	
	public static void setPopup(Rect nameRect, List<String> list, Consumer<String> func) {
		popup = new Popup(nameRect, list, func);
	}
	
	public static void drawPopup() {
		if(popup != null)  {
			popup = popup.draw();
		}
	}
	
	public static void removePopup() {
		popup = null;
	}
	
	public static String textField(Rect r, String name, String v, float padding) {
		beginArea(new Rect(r.x, r.y, padding, r.height));
		label(name, 0, 0);
		endArea();
		
		// This is temp has to change
		if(buttonReleased(v, new Rect(r.x + padding, r.y, r.width - padding, r.height), "Box", "Box")) {
			String s = tinyfd_inputBox("Changing " + name + "!", "What would you like this varible to be?", v);
			if(s!=null)
				s = s.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", "");
				if(s != "" && !s.startsWith(" "))
					return s;
				else
					Debug.log("Invalid name! A name cannot start with a space! ue#0002");
		}
		return v;
	}
	
	public static float floatField(Rect r, String name, Float v, float padding) {
		String ret = GUI.textField(r, name, String.valueOf(v), padding);
		float f = v;
		
		try { f = Float.parseFloat(ret); }
		catch (NumberFormatException e) { Debug.log("Float field input is inviliad! ue#0001"); }
		return f;
	}
	
	public static Vector2f vectorField(Rect r, String name, Vector2f v, float padding) {
		beginArea(new Rect(r.x, r.y, padding, r.height));
		label(name, 0, 0);
		endArea();
		
		float half = (r.width - padding) / 2.0f;
		float x = floatField(new Rect(r.x + padding - 5, r.y, half, r.height), "x", v.x, 10);
		float y = floatField(new Rect(r.x + padding + half, r.y, half, r.height), "y", v.y, 10);
		return new Vector2f(x, y);
	}
	
	public static boolean buttonReleased(String text, Rect r, String normalStyle, String hoverStyle) {
		return buttonReleased(text, r, skin.getStyle(normalStyle), skin.getStyle(hoverStyle));
	}
	
	public static boolean buttonReleased(String text, Rect r, GUIStyle normalStyle, GUIStyle hoverStyle) {
		int realesed = button(text, r, normalStyle, hoverStyle);
		if(realesed == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean buttonDown(String text, Rect r, String normalStyle, String hoverStyle) {
		return buttonDown(text, r, skin.getStyle(normalStyle), skin.getStyle(hoverStyle));
	}
	
	public static boolean buttonDown(String text, Rect r, GUIStyle normalStyle, GUIStyle hoverStyle) {
		int down = button(text, r, normalStyle, hoverStyle);
		return down == 2;
	}
	
	public static boolean buttonPressed(String text, Rect r, String normalStyle, String hoverStyle) {
		return buttonPressed(text, r, skin.getStyle(normalStyle), skin.getStyle(hoverStyle));
	}
	
	public static boolean buttonPressed(String text, Rect r, GUIStyle normalStyle, GUIStyle hoverStyle) {
		int pressed = button(text, r, normalStyle, hoverStyle);
		return pressed == 1;
	}
	
	private static int button(String text, Rect r, GUIStyle normalStyle, GUIStyle hoverStyle) {
		Rect rf = r.copy();
		GUIArea a = areas.get(area);
		rf.addPosition(a.area); 
		rf.y -= a.getScroll();
		
		if(rf.inRect(Application.input.getMousePosition())) {
			Rect p = box(r, hoverStyle);
			if(p!=null)
				label(text, p.x, p.y);
			else
				label(text, r.x, r.y);
			
			if(Application.input.isButtonPressed(0)) return 1;
			if(Application.input.isButtonDown(0)) return 2;
			if(Application.input.isButtonReleased(0)) return 0;
		}
		else {
			Rect p = box(r, normalStyle);
			if(p!=null)
				label(text, p.x, p.y);
			else
				label(text, r.x, r.y);
		}
		return -1;
	}
	
	public static int centeredButton(String text, Rect r, GUIStyle normalStyle, GUIStyle hoverStyle) {
		Rect rf = r.copy();
		GUIArea a = areas.get(area);
		rf.addPosition(a.area); 
		rf.y -= a.getScroll();
		
		float x = rf.x + ((rf.width / 2f) - ((float) font.getStringWidth(text) / 2f));
		float y = rf.y + ((rf.height / 2f) - (font.getFontHeight() / 2f));
		
		if(rf.inRect(Application.input.getMousePosition())) {
			box(r, hoverStyle);
			label(text, x, y);
			
			if(Application.input.isButtonPressed(0)) return 1;
			if(Application.input.isButtonDown(0)) return 2;
			if(Application.input.isButtonReleased(0)) return 0;
		}
		else {
			box(r, normalStyle);
			label(text, x, y);
		}
		return -1;
	}

	public static boolean toggle(boolean b, float x, int y, GUIStyle on, GUIStyle off) {
		GUIStyle s;
		if(b)
			s = on;
		else 
			s = off;
		
		if(GUI.buttonReleased("", new Rect(x, y, 15, 15), s, s))
			return !b;
		return b;
	}

	public static Rect box(Rect r, String style) {
		return box(r, skin.getStyle(style));
	}
	
	public static Rect box(Rect r, GUIStyle e) {
		//If there is no style, return null because it needs a style to return the center of it
		if(e == null) return null;
		
		//Cache a short variable for the texture, just so we only have to type a character anytime we use it
		Texture t = skin.texture;
		
		//Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect tl = new Rect(r.x, r.y, e.padding.x, e.padding.y);
		Rect tlu = new Rect(e.uv.x, e.uv.y, e.paddingUV.x, e.paddingUV.y);
		drawTextureWithTextCoords(t, tl, tlu);
		
		//Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect tr = new Rect((r.x + r.width) - e.padding.width, r.y, e.padding.width, e.padding.y);
		Rect tru = new Rect((e.uv.x + e.uv.width) - e.paddingUV.width, e.uv.y, e.paddingUV.width, e.paddingUV.y);
		drawTextureWithTextCoords(t, tr, tru);
		
		//Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect bl = new Rect(r.x, (r.y + r.height) - e.padding.height, e.padding.x, e.padding.height);
		Rect blu = new Rect(e.uv.x, (e.uv.y + e.uv.height) - e.paddingUV.height, e.paddingUV.x, e.paddingUV.height);
		drawTextureWithTextCoords(t, bl, blu);
		
		//Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
		Rect br = new Rect(tr.x, bl.y, e.padding.width, e.padding.height);
		Rect bru = new Rect(tru.x, blu.y, e.paddingUV.width, e.paddingUV.height);
		drawTextureWithTextCoords(t, br, bru);
		
		//Get the left side of the box using corresponding padding values and draw it using a texture drawing method
		Rect l = new Rect(r.x, r.y + e.padding.y, e.padding.x, r.height - (e.padding.y + e.padding.height));
		Rect lu = new Rect(e.uv.x, e.uv.y + e.paddingUV.y, e.paddingUV.x, e.uv.height - (e.paddingUV.y + e.paddingUV.height));
		drawTextureWithTextCoords(t, l, lu);
		
		//Get the right side of the box using corresponding padding values and draw it using a texture drawing method
		Rect ri = new Rect(tr.x, r.y + e.padding.y, e.padding.width, l.height);
		Rect ru = new Rect(tru.x, lu.y, e.paddingUV.width, lu.height);
		drawTextureWithTextCoords(t, ri, ru);
		
		//Get the top of the box using corresponding padding values and draw it using a texture drawing method
		Rect ti = new Rect(r.x + e.padding.x, r.y, r.width - (e.padding.x + e.padding.width), e.padding.y);
		Rect tu = new Rect(e.uv.x + e.paddingUV.x, e.uv.y, e.uv.width - (e.paddingUV.x + e.paddingUV.width), e.paddingUV.y);
		drawTextureWithTextCoords(t, ti, tu);
		
		//Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
		Rect b = new Rect(ti.x, bl.y, ti.width, e.padding.height);
		Rect bu = new Rect(tu.x, blu.y, tu.width, e.paddingUV.height);
		drawTextureWithTextCoords(t, b, bu);
		
		//Get the center of the box using corresponding padding values and draw it using a texture drawing method
		Rect c = new Rect(ti.x, l.y, ti.width, l.height);
		Rect cu = new Rect(tu.x, lu.y, tu.width, lu.height);
		drawTextureWithTextCoords(t, c, cu);
		
		//Return the center rectangle
		return c;
	}
	
	public static void label(String text, float x, float y) {
		Map<Character, Glyph> chars = font.getChars();
		
		c = text.toCharArray();
		tempX = x;
		for(i = 0; i < c.length; i++) {
			Glyph glyph = chars.get(c[i]);
			
			drawTextureWithTextCoords(font.getTexture(), new Rect(tempX, y, glyph.scale.x, glyph.scale.y), new Rect(glyph.position.x, glyph.position.y, glyph.size.x, glyph.size.y), textColor);

			tempX += glyph.scale.x;
		}
		Texture.resetBound();
	}
	
	public static void unbind() {
		shader.unbind();
		mesh.unbind();
	}
	
	public static void drawTexture(Texture tex, Rect r) {
		drawTextureWithTextCoords(tex, r, new Rect(0, 0, 1, 1));
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect) {
		drawTextureWithTextCoords(tex, drawRect, uvRect, tintColor);
	}
	
	public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect, Color c) {
		GUIArea a = areas.get(area);
		if(a.area == null) return;
		Rect r = a.area.getIntersection(new Rect(drawRect.x + a.area.x, (drawRect.y + a.area.y) - a.getScroll(), drawRect.width, drawRect.height));
		
		if(r == null) return;
		
		float x = uvRect.x + ((((r.x - drawRect.x) - a.area.x) / drawRect.width) * uvRect.width);
		float y = uvRect.y + ((((r.y - drawRect.y) - (a.area.y - a.getScroll())) / drawRect.height) * uvRect.height);
		Rect u = new Rect(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);
		
		tex.bind();
		
		shader.setUniform("offset", u.x, u.y, u.width, u.height);
		shader.setUniform("pixelScale", r.width, r.height);
		shader.setUniform("screenPos", r.x, r.y);
		shader.setMatColor(c);
		
		mesh.render();
	}
	
	public static void cleanup() {
		shader.cleanup();
	}
	
	public static void window(Rect r, String title, Consumer<Rect> f, String style) {
		window(r, title, f, skin.getStyle(style));
	}
	
	public static void window(Rect r, String title, Consumer<Rect> f, GUIStyle style) {
		Rect center = r;
		if(style != null) {
			center = box(r, style);
			label(title, r.x + style.padding.x, r.y+4);
			beginArea(center);
		}
		else {
			label(title, r.x, r.y);
			beginArea(r);
		}
		
		f.accept(center);
		endArea();
	}
	
	public static int setScrollView(int scrollHeight, int offset) {
		GUIArea a = areas.get(area);
		a.scrollHeight = scrollHeight;
		return a.scroll(offset);
	}
	
	public static void beginArea(Rect r) {
		GUIArea a = areas.get(area);
		areas.add(new GUIArea(a.area.getIntersection(new Rect(a.area.x + r.x, (a.area.y + r.y) - a.getScroll(), r.width, r.height))));
		area = areas.size()-1;
	}
	
	public static void endArea() {
		if(areas.size() == 1)
			return;
		
		areas.remove(areas.size()-1);
		area = areas.size()-1;
	}
 }
