package dk.sebsa.blackfur.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.print.Printable;
import java.io.IOException;

import dk.sebsa.blackfur.editor.Editor;
import dk.sebsa.blackfur.game.Test;
import dk.sebsa.blackfur.game.Tets;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.Sprite;
import dk.sebsa.blackfur.math.Color;
import dk.sebsa.blackfur.math.Vector2f;

public class Core {
	private static long window;
	private static Color clearColor;
	
	public static void main(String[] args) throws IOException {
		try {
			window = Application.init();
			clearColor = Color.black();
			
			// Test component
			Entity entity = new Entity("Jens");
			entity.addComponent(new Test());
			entity.addComponent(new Tets());
			
			// Init statics
			GUI.init();
			Editor.init();
			Renderer.init();
			
			// Test rendering
			new Texture("SMW-Tileset.png");
			new Material("SMWTiles");
			Sprite s = new Sprite("test");
			SpriteRenderer sr = new SpriteRenderer();
			entity.addComponent(sr);
			sr.sprite = s;
			entity.setScale(new Vector2f(2, 2));
				
			// Setup alpha
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			while(!glfwWindowShouldClose(window)) {
				// Update Application
				Application.update(clearColor);
				
				// Render the editor
				Editor.render();
				
				Renderer.addToRender(sr);
				Renderer.render(new Rect(400, 0, Application.getWidth()-800, Application.getHeight() - 30));
				
				// End frame
				Application.lateUpdate();
				glfwSwapBuffers(window);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {	
			Texture.cleanup();
			GUI.cleanup();
			Mesh.cleanupAll();
			glfwTerminate();
		}
	}
}
