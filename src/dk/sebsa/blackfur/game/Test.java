package dk.sebsa.blackfur.game;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.blackfur.engine.Application;
import dk.sebsa.blackfur.engine.Component;
import dk.sebsa.blackfur.engine.Time;

public class Test extends Component {
	public String thing = "Hello";
	public int world = 1;
	public float flota = 1.48f;
	
	public void update() {
		if(Application.input.isKeyDown(GLFW.GLFW_KEY_SPACE))
			entity.setRotation(entity.getRotation() + (Time.getDeltaTime() * 20));
		else if(Application.input.isKeyDown(GLFW.GLFW_KEY_ENTER))
			entity.setRotation(entity.getRotation() - (Time.getDeltaTime() * 20));
	}
}
