package dk.sebsa.blackfur.input;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.math.Convert;
import dk.sebsa.blackfur.math.Vector2f;

enum CoursorImage{Pointer, Hand, HScroll, VScroll}
public class Input {
	private byte[] keys;
	private byte[] keysPressed;
	private byte[] keysReleased;
	private byte[] buttons;
	private byte[] buttonsPressed;
	private byte[] buttonsReleased;
	private double mouseX;
	private double mouseY;
	private double prevMouseX;
	private double prevMouseY;
	private double scrollX;
	private double scrollY;
	private final Vector2f displVec;
	private byte inWindow = 0;
	private final long window;
	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;
	private GLFWCursorEnterCallback cursorEnter;
	private Vector2f position;
	
	public Input() {
		// Bool to byte
		keys = Convert.keys;
		keysPressed = Convert.keys.clone();
		keysReleased = Convert.keys.clone();
		buttons = Convert.buttons.clone();
		buttonsPressed = Convert.buttons.clone();
		buttonsReleased = Convert.buttons.clone();
		
		displVec = new Vector2f();
		position = new Vector2f();
		this.window = Application.getWindow();
		
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key != -1) {
					keys[key] = (byte)(action != GLFW.GLFW_RELEASE?1:0);
					if(action == 1) {
						keysPressed[key] = 1;
					}
					if(action == 0) {
						keysReleased[key] = 1;
					}
				}
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (byte)(action != GLFW.GLFW_RELEASE?1:0);
				if(action == 1) {
					buttonsPressed[button] = 1;
				}
				if(action == 0) {
					buttonsReleased[button] = 1;
				}
			}
		};
		
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
		
		cursorEnter = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long windowId, boolean entered) {
				inWindow = (byte)(entered?1:0);
			}
		};
	}
	
	public boolean isKeyDown(int key) {
		System.out.println(keys[key]);
		return keys[key] == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return keysPressed[key] == 1;
	}
	
	public boolean isKeyReleased(int key) {
		return keysReleased[key] == 1;
	}
	
	public boolean isButtonDown(int button) {
		return buttons[button] == 1;
	}
	
	public boolean isButtonPressed(int button) {
		return buttonsPressed[button] == 1;
	}
	
	public boolean isButtonReleased(int button) {
		return buttonsReleased[button] == 1;
	}
	
	public void cleanup(long windowId) {
		glfwFreeCallbacks(windowId);
	}

	public double getMouseX() {
		return mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}
	
	public double getScrollX() {
		return scrollX;
	}

	public double getScrollY() {
		return scrollY;
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
	
	public void update() {
		// displayVec
		displVec.x = 0;
        displVec.y = 0;
        if (prevMouseX > 0 && prevMouseY > 0 && inWindow == 1) {
            double deltax = mouseX - prevMouseX;
            double deltay = mouseY - prevMouseY;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        prevMouseX = mouseX;
        prevMouseY = mouseY;
	}
	
	public void late() {
		keysPressed = Convert.keys.clone();
		keysReleased = Convert.keys.clone();
		buttonsPressed = Convert.buttons.clone();
		buttonsReleased = Convert.buttons.clone();
	}
	
	public void centerCursor() {
		if (inWindow == 1) {
			glfwSetCursorPos(window, Application.getWidth() / 2, Application.getHeight() / 2);
			prevMouseX = Application.getWidth() / 2;
			prevMouseY = Application.getHeight() / 2;
		}
	}
	
	public void centerCursor2() {
		if (inWindow == 1) {
			glfwSetCursorPos(window, Application.getWidth() / 2, Application.getHeight() / 2);
			prevMouseX = Application.getWidth() / 2;
			prevMouseY = Application.getHeight() / 2;
			mouseX = prevMouseX;
			mouseY = prevMouseY;
			displVec.zero();
		}
	}

	public Vector2f getDisplVec() {
		return displVec;
	}
	
	public GLFWCursorEnterCallback getCursorEnterCallback() {
		return cursorEnter;
	}
	
	public Vector2f getMousePosition() {
		position.x = (float) mouseX;
		position.y = (float) mouseY;
		return position;
	}
}

