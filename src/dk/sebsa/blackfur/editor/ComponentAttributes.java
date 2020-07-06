package dk.sebsa.blackfur.editor;

import java.lang.reflect.Field;

import dk.sebsa.blackfur.engine.Component;

public class ComponentAttributes {
	public Component component;
	public Class<?> c;
	public String[] fields;
	public int height;
	
	public ComponentAttributes(Component component) {
		this.component = component;
		c = component.getClass();
		
		Field[] f = c.getFields();
		fields = new String[f.length - 1];
		
		for(int i = 0; i < fields.length; i++) {
			String[] splitName = f[i].getType().toString().split("\\.");
			fields[i] = f[i].getName() + " " + splitName[splitName.length-1];
			
			if(i < fields.length - 1) height += 24;
			else height += 22;
		}
	}
	
	
}
