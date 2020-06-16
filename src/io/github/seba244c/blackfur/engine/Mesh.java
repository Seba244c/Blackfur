package io.github.seba244c.blackfur.engine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {
	private int v_id;
	private int u_id;
	private int vao;
	
	/*private float[] verticies;
	private float[] uvs; UNUSED*/
	private static List<Mesh> meshs = new ArrayList<Mesh>();
	
	public Mesh(float[] verticies, float[] uvs) {
		/*this.verticies = verticies;
		this.uvs = uvs; UNUSED*/
		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		v_id = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, CreateBuffer(verticies), GL30.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, verticies.length/3, GL30.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		u_id = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, CreateBuffer(uvs), GL30.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		meshs.add(this);
	}
	
	public void render() {
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
	}
	
	public void bind() {
		GL30.glBindVertexArray(vao);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
		GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
	}
	
	public void unbind() {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public FloatBuffer CreateBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public void cleanup() {
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(v_id);
		GL15.glDeleteBuffers(u_id);
	}
	
	public static void cleanupAll() {
		for(int i = 0; i < meshs.size(); i++) {
			meshs.get(i).cleanup();
		}
		meshs.clear();
	}
}
