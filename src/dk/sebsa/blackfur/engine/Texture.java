package dk.sebsa.blackfur.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.util.List;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Texture {
	private String name = "";
	
	private int id;
	private int width, height;
	
	private static List<Texture> textureInstances = new ArrayList<Texture>();
	private static Texture tmp = null;
	
	private static int i = 0;
	private static int boundTexture = -1;
	
	public Texture(String filename) {
		name = filename;
		
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
		
		ByteBuffer dataBuffer = stbi_load("./res/Textures/"+filename, widthBuffer, heightBuffer, channelsBuffer, 4);
		
		id = glGenTextures();
		width = widthBuffer.get();
		height = heightBuffer.get();
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, dataBuffer);
		stbi_image_free(dataBuffer);
		
		textureInstances.add(this);
	}
	
	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public int getId() {
		return id;
	}
	
	public void bind() {
		if(boundTexture!=id)
			glBindTexture(GL_TEXTURE_2D, id);
		boundTexture=id;
	}
	
	public static Texture findTexture(String textureName) {
		for(i = 0; i < textureInstances.size(); i++) {
			tmp = textureInstances.get(i);
			if(tmp.name.startsWith(textureName)) {
				return tmp;
			}
		}
		return null;
	}
	
	public static void cleanup() {
		for(i = 0; i < textureInstances.size(); i++) {
			glDeleteTextures(textureInstances.get(i).getId());
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static List<Texture> getTextureInstances() {
		return textureInstances;
	}
	
	public static void resetBound() {
		boundTexture = -1;
	}
}