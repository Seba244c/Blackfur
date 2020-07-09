package dk.sebsa.blackfur.engine;

import dk.sebsa.blackfur.gui.Sprite;
import dk.sebsa.blackfur.math.Vector2f;

public class SpriteRenderer extends Component {
	public Sprite sprite;
	private Vector2f scale;
	
	public void setUniforms() {
		sprite.material.shader.setUniform("screenPos", entity.getPosition().x, entity.getPosition().y);
		
		scale = entity.getScale();
		sprite.material.shader.setUniform("pixelScale", sprite.offset.width * scale.x, sprite.offset.height * scale.y);
		
		Rect uvRect = sprite.getUV();
		sprite.material.shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
	}
	
	public void prepare() {
		Renderer.addToRender(this);
	}
}
