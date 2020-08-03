package dk.sebsa.blackfur.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import dk.sebsa.blackfur.input.Input;

public class Application {
	public static String name = "Blackfur Engine";
	private static int width = 1280;
	private static int height = 720;
	public static boolean resized;
	private static long window;
	private static Rect r;
	private static byte minemiszed = 0;
	
	public static Input input;
	private static GLFWWindowSizeCallback sizeCallback;
	
	@SuppressWarnings("resource")
	public static long init() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		//If glfw cannot be initialized
		if(!glfwInit())
			throw new IllegalStateException("GLFW could not be initialized");
		
		// Set Window Rect
		r = new Rect(0, 0, width, height);
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		// OSX Sipport
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// Create the window
		window = glfwCreateWindow(width, height, name, 0, 0);
		
		// Show Window
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		
		// Setup opengl
		GL.createCapabilities();
		
		// Setup input
		input = new Input();
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glfwSetKeyCallback(window, input.getKeyboardCallback());
		glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		glfwSetScrollCallback(window, input.getMouseScrollCallback());
		glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		glfwSetCursorEnterCallback(window, input.getCursorEnterCallback());
		
		// Resize callback
		sizeCallback = GLFWWindowSizeCallback.create(Application::onResize);
		glfwSetWindowSizeCallback(window, sizeCallback);
		
		// Vsync
		glfwSwapInterval(1);
		
		return window;
	}
	
	public static void onResize(long window, int w, int h) {
		if(w == 0 && h == 0 ) minemiszed = 1;
		else minemiszed = 0;
		width = w;
        height = h;
        r.set(0, 0, w, h);
        glViewport(0, 0, w, h);
        resized = true;
        Renderer.updateFBO(w, h);
	}
	
	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static long getWindow() {
		return window;
	}
	
	public static void cleanup() {
		input.cleanup(window);
		sizeCallback.close();
	}

	public static Rect getRect() {
		return r;
	}

	public static final boolean isMinemiszed() {
		return minemiszed == 1;
	}
}
