package dk.sebsa.blackfur.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.math.Matrix4x4;

public class Renderer {
	private static Map<Material, List<SpriteRenderer>> batch = new HashMap<Material, List<SpriteRenderer>>();
	private static Mesh mesh;
	private static Matrix4x4 projection;
	private static FBO fbo;
	
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
		
		updateFBO(Application.getWidth(), Application.getHeight());
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
		fbo.bindFrameBuffer();
		glClearColor(0, 1, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT);
		
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
				material.shader.bind();
				renderer.setUniforms();
				mesh.render();
			}
			
			material.unbind();
		}
		
		mesh.unbind();
		batch.clear();
		fbo.unBind();
		
		GUI.prepare();
		GUI.drawTextureWithTextCoords(fbo.getTexture(), r, new Rect(r.x / w, r.y / h, r.width / w, r.height / h));
		GUI.unbind();
	}
	
	public static void updateFBO(int width, int height) {
		fbo = new FBO(width, height);
	}
}
