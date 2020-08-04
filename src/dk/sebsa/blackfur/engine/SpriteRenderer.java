package dk.sebsa.blackfur.engine;

import dk.sebsa.blackfur.gui.Sprite;
import dk.sebsa.blackfur.math.Vector2f;

public class SpriteRenderer extends Component {
	public Sprite sprite;
	public Vector2f anchor = new Vector2f(0, 0);
	
	public void setUniforms() {
		sprite.material.shader.setUniform("anchor", anchor);
		
		if(entity.isDirty()) { entity.updateMatrix(); }
		sprite.material.shader.setUniform("transformMatrix", entity.getMatrix());
		sprite.material.shader.setUniform("objectScale", entity.getScale());
		
		sprite.material.shader.setUniform("screenPos", entity.getPosition());
		
		sprite.material.shader.setUniform("pixelScale", sprite.offset.width, sprite.offset.height);
		
		Rect uvRect = sprite.getUV();
		sprite.material.shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
	}
	
	public void prepareToRender() {
		Renderer.addToRender(this);
	}
}
