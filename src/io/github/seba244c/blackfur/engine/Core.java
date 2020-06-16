package io.github.seba244c.blackfur.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.IOException;

import io.github.seba244c.blackfur.editor.Editor;
import io.github.seba244c.blackfur.gui.GUI;
import io.github.seba244c.blackfur.math.Color;

public class Core {
	private static long window;
	private static Color clearColor;
	
	public static void main(String[] args) throws IOException {
		try {
			window = Application.Init();
			clearColor = Color.black();
			
			// Init gui
			GUI.init();
			Editor.init();
						
			// Setup alpha
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			for(int z = 0; z < 50; z++) {
				Debug.Log(z);
			}
			
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
