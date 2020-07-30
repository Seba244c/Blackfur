package dk.sebsa.blackfur.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.sebsa.blackfur.engine.Texture;
import dk.sebsa.blackfur.math.Vector2f;

public class Font {
	private String name;
	private int fontID;
	private BufferedImage bufferedImage;
	private Vector2f imageSize;
	private java.awt.Font font;
	private FontMetrics fontMetrics;
	private int i;
	private Texture texture;
	
	private static List<Font> fonts = new ArrayList<Font>();
	private Map<Character, Glyph> chars = new HashMap<Character, Glyph>();
	
	@SuppressWarnings("resource")
	public Font(String name, float size) {		
		try {
			font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, Font.class.getResourceAsStream("/Font/" + name + ".ttf")).deriveFont(size);
		} catch (FontFormatException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		
		//try {
		//	font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("res/Font/"+name+".ttf")).deriveFont(size);
		//} catch (FontFormatException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		
		generateFont();
		this.name = name;
		fonts.add(this);
	}
	
	public Font(java.awt.Font font) {
		this.font = font;
		generateFont();
		this.name = font.getFontName();
		fonts.add(this);
	}
	
	private void generateFont() {
		GraphicsConfiguration graphCon = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Graphics2D graphics = graphCon.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
		graphics.setFont(font);
		
		fontMetrics = graphics.getFontMetrics();
		imageSize = new Vector2f(2048, 2048);
		bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) imageSize.x, (int) imageSize.y, Transparency.TRANSLUCENT);
		
		fontID = glGenTextures();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, fontID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int)imageSize.x, (int)imageSize.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, generateImage());
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		texture = new Texture(fontID, (int)imageSize.x, (int)imageSize.y);
	}
	
	private ByteBuffer generateImage() {
		Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();
		graphics2d.setFont(font);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		drawCharaters(graphics2d);
		return createBuffer();
	}

	private ByteBuffer createBuffer() {
		int w = (int)imageSize.x;
		int h = (int)imageSize.y;
		int[] pixels = new int[w*h];
		
		bufferedImage.getRGB(0, 0, w, h, pixels, 0, w);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(w * h * 4);
		
		for(i = 0; i < pixels.length; i++) {
			byteBuffer.put((byte) ((pixels[i] >> 16) & 0xFF)); 	// Red
			byteBuffer.put((byte) ((pixels[i] >> 8) & 0xFF)); 	// Green
			byteBuffer.put((byte) (pixels[i] >> 0xFF)); 		// Blue
			byteBuffer.put((byte) ((pixels[i] >> 24) & 0xFF)); 	// Alpha
		}
		byteBuffer.flip();
		return byteBuffer;
	}

	private void drawCharaters(Graphics2D graphics2d) {
		int tempX = 0;
		int tempY = 0;
		float h = (float) (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());
		
		for(i=32; i < 256; i++) {
			if(i==127) continue;
			
			char c = (char) i;
			float charWidth = fontMetrics.charWidth(c);
			
			float advance = charWidth + 8;
			
			if(tempX + advance > imageSize.x) {
				tempX = 0;
				tempY += 1;
			}
			
			chars.put(c, new Glyph(new Vector2f(tempX / imageSize.x, (tempY * h) / imageSize.y), new Vector2f(charWidth / imageSize.x, h/imageSize.y), new Vector2f(charWidth, h)));
			graphics2d.drawString(String.valueOf(c), tempX, fontMetrics.getMaxAscent() + (h* tempY));
			tempX += advance;
		}
	}

	public int getFontID() {
		return fontID;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public Map<Character, Glyph> getChars() {
		return chars;
	}
	
	public int getStringWidth(String s) {
		return fontMetrics.stringWidth(s);
	}
	
	public final String getName() {return name;}
	
	public static final List<Font> getFonts() {
		return fonts;
	}
}
