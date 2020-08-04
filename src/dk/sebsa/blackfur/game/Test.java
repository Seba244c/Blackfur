package dk.sebsa.blackfur.game;

import dk.sebsa.blackfur.engine.Component;
import dk.sebsa.blackfur.engine.Time;

public class Test extends Component {
	public String thing = "Hello";
	public int world = 1;
	public float flota = 1.48f;
	
	public void update() {
		entity.setRotation(entity.getRotation() + (Time.getDeltaTime() * 10));
	}
}
