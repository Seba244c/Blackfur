package dk.sebsa.blackfur.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL20;

import dk.sebsa.blackfur.math.Color;
import dk.sebsa.blackfur.math.Matrix4x4;

public class Shader {
	private int program;
	private int vs;
	private int fs;
	private static Color boundColor = Color.transparent();
	
	public Shader(String fileName) throws IOException {
		// Program
		program = GL20.glCreateProgram();
		if (program == 0)
            throw new IllegalStateException("Could not create Shader");
        
		
		// Vertex
		vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		if (vs == 0)
            throw new IllegalStateException("Error creating shader. Type: Vertex Shader");
        
		GL20.glShaderSource(vs, CreateShader(fileName + ".vs"));
		GL20.glCompileShader(vs);
		
		if(GL20.glGetShaderi(vs, GL20.GL_COMPILE_STATUS) != 1) 
			throw new IOException("Error compiling Shader code: " + GL20.glGetShaderInfoLog(vs, 1024));
		
		// Fragment
		fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		if (vs == 0)
            throw new IllegalStateException("Error creating shader. Type: Vertex Shader");
        
		GL20.glShaderSource(fs, CreateShader(fileName + ".fs"));
		GL20.glCompileShader(fs);
		
		if(GL20.glGetShaderi(fs, GL20.GL_COMPILE_STATUS) != 1) 
			throw new IOException("Error compiling Shader code: " + GL20.glGetShaderInfoLog(fs, 1024));
		
		// Attach shaders to program
		GL20.glAttachShader(program, vs);
		GL20.glAttachShader(program, fs);
		
		// Bind attrib
		GL20.glBindAttribLocation(program, 0, "verticies");
		GL20.glBindAttribLocation(program, 1, "uv");
		
		// Link, validate and check program
		GL20.glLinkProgram(program);
		
		if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) {
            throw new UnknownError("Error linking Shader code: " + GL20.glGetProgramInfoLog(program, 1024));
        }
		
		GL20.glValidateProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == 0) {
        	System.err.println("Warning validating Shader code: " + GL20.glGetProgramInfoLog(program, 1024));
        }
	}
	
	@SuppressWarnings("resource")
	private String CreateShader(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File("./res/Shaders/"+fileName)));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	public void setUniform(String name, Color c) {
		int location = GL20.glGetUniformLocation(program, name);
		if(location != -1) GL20.glUniform4f(location, c.r, c.g, c.b, c.a);
	}
	
	public void setUniform(String name, float x, float y) {
		int location = GL20.glGetUniformLocation(program, name);
		if(location != -1) GL20.glUniform2f(location, x, y);
	}
	
	public void setUniform(String name, float x, float y, float z, float w) {
		int location = GL20.glGetUniformLocation(program, name);
		if(location != -1) GL20.glUniform4f(location, x, y, z, w);
	}
	
	public void setUniform(String name, Matrix4x4 matrix) {
		int location = GL20.glGetUniformLocation(program, name);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.getBuffer(buffer);
		
		if(location != -1) GL20.glUniformMatrix4fv(location, false, buffer);
		buffer.flip();
	}
	
	public void setMatColor(Color c) {
		if(!boundColor.Compare(c)) {
			setUniform("matColor", c);
			boundColor = c;
		}
	}
	
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
        unbind();
        if (program != 0) {
            GL20.glDeleteProgram(program);
        }
    }

	public static Color getBoundColor() {
		return boundColor;
	}
}
