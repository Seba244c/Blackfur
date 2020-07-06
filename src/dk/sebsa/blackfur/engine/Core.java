package dk.sebsa.blackfur.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.IOException;

import dk.sebsa.blackfur.editor.Editor;
import dk.sebsa.blackfur.game.Test;
import dk.sebsa.blackfur.game.Tets;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.math.Color;

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
			
			// Init gui
			GUI.init();
			Editor.init();
						
			// Setup alpha
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			while(!glfwWindowShouldClose(window)) {
				// Update Application
				Application.update(clearColor);
				
				// Render the editor
				Editor.render();
				
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
