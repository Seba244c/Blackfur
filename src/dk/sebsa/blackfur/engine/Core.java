package dk.sebsa.blackfur.engine;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.IOException;

import dk.sebsa.blackfur.editor.Editor;
import dk.sebsa.blackfur.editor.EditorUtil;
import dk.sebsa.blackfur.editor.EngineBootLoader;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.math.Color;

public class Core {
	private static long window;
	private static Color clearColor;
	
	public static void main(String[] args) throws IOException {
		try {
			// Init config
			Editor.initConfig();
			EngineBootLoader.init();
			
			// Init window
			window = Application.init();
			clearColor = Color.black();
			
			AssetDatabase.loadAllResources();
			
			// Init statics
			GUI.init();
			Editor.init();
			Renderer.init();
			Time.init();
			
			// Load scene
			SceneManager.loadScene("New Scene");
		
			// Setup alpha
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			while(!glfwWindowShouldClose(window)) {
				// Poll events
				glfwPollEvents();
				
				if(!Application.isMinemiszed()) {
					// Update base updates
					glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
					Application.input.update();
					Application.resized = false;
					Time.process();
					
					// run component methods
					Entity.prepareEntities();
					
					// Rendering
					Editor.render();
					
					if(Editor.isPlaying())
						Renderer.render(new Rect(0, 30, Application.getWidth(), Application.getHeight() - 60));
					else
						Renderer.render(new Rect(400, 30, Application.getWidth()-800, Application.getHeight() - 260));
					
					glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
					
					// GUI stuff not in editor
					GUI.prepare();
					
					GUI.label(String.valueOf(Time.getFrameRate()), 10, Application.getHeight() / 2);
					GUI.drawPopup();
					
					GUI.unbind();
					
					// End frame
					Application.input.late();
				}
				glfwSwapBuffers(window);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {	
			Texture.cleanup();
			GUI.cleanup();
			Mesh.cleanupAll();
			EditorUtil.cleanUp();
			glfwTerminate();
		}
	}
}
