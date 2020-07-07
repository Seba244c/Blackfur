package dk.sebsa.blackfur.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.sebsa.blackfur.math.Matrix4x4;

public class Renderer {
	private static Map<Material, List<SpriteRenderer>> batch = new HashMap<Material, List<SpriteRenderer>>();
	private static Mesh mesh;
	private static Matrix4x4 projection;
	
	public static void init() {
		float[] square = new float[] {
				0, 1, 1, 1, 1, 0,
				1, 0, 0, 0, 0, 1
		};
		float[] uv = new float[] {
				0, 0, 1, 0, 1, 1,
				1, 1, 0, 1, 0, 0
		};
		mesh = new Mesh(square, uv);
	}
	
	public static void addToRender(SpriteRenderer renderer) {
		List<SpriteRenderer> matRenderers = batch.get(renderer.sprite.material);
		if(matRenderers == null) {
			matRenderers = new ArrayList<SpriteRenderer>();
			batch.put(renderer.sprite.material, matRenderers);
		}
		
		matRenderers.add(renderer);
	}
	
	public static void render(Rect r) {
		float w = Application.getWidth();
		float h = Application.getHeight();
		float halfW = w * 0.5f;
		float halfH = h * 0.5f;
		
		projection = Matrix4x4.ortho(-halfW, halfW, halfH, -halfH, -1, 1);
		
		mesh.bind();
		
		for(Material material : batch.keySet()) {
			material.bind();
			material.shader.setUniform("projection", projection);
			material.shader.setMatColor(material.color);
			
			List<SpriteRenderer> renderers = batch.get(material);
			for(SpriteRenderer renderer : renderers) {
				renderer.setUniforms();
				mesh.render();
			}
			
			material.unbind();
		}
		
		mesh.unbind();
		batch.clear();
	}
}
