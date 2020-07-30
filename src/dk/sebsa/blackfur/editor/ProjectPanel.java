package dk.sebsa.blackfur.editor;

import java.util.List;

import dk.sebsa.blackfur.engine.DataType;
import dk.sebsa.blackfur.engine.Material;
import dk.sebsa.blackfur.engine.Rect;
import dk.sebsa.blackfur.engine.Shader;
import dk.sebsa.blackfur.engine.Texture;
import dk.sebsa.blackfur.gui.Font;
import dk.sebsa.blackfur.gui.GUI;
import dk.sebsa.blackfur.gui.GUISkin;
import dk.sebsa.blackfur.gui.GUIStyle;
import dk.sebsa.blackfur.gui.Sprite;

public class ProjectPanel
{
	private GUIStyle box;
	private GUIStyle empty = null;
	private Rect temp = new Rect(0, 0, 0, 0);
	private DataType selectedType = DataType.Texture;
	private int i;
	
	public ProjectPanel()
	{
		box = Editor.skin.getStyle("Box");
	}
	
	public void renderTypes(Rect r)
	{
		DataType[] values = DataType.values();
		for(int i = 0; i < values.length; i++)
		{
			temp.set(0, i * 26, r.width, 26);
			if(!values[i].equals(selectedType))
			{
				if(GUI.buttonReleased(values[i].name() + "s", temp, empty, empty)) selectedType = values[i];
			}
			else
			{
				Rect s = GUI.box(temp, box);
				GUI.beginArea(s);
				GUI.label(values[i].name() + "s", 0, 0);
				GUI.endArea();
			}
		}
	}
	
	public void renderAssets(Rect r)
	{
		Object selected = Editor.getSelectedAsset();
		
		if(selectedType == DataType.Font)
		{
			List<Font> fonts = Font.getFonts();
			for(i = 0; i < fonts.size(); i++)
			{
				if(selected != null)
				{
					if(fonts.get(i).equals(selected))
					{
						if(GUI.buttonReleased(fonts.get(i).getName(), new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) fonts.get(i));
					}
					else if(GUI.buttonReleased(fonts.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) fonts.get(i));
				}
				else if(GUI.buttonReleased(fonts.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) fonts.get(i));
			}
		}
		else if(selectedType == DataType.Material)
		{
			List<Material> materials = Material.getMaterials();
			for(i = 0; i < materials.size(); i++)
			{
				if(selected != null)
				{
					if(materials.get(i).equals(selected))
					{
						if(GUI.buttonReleased(materials.get(i).name, new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) materials.get(i));
					}
					else if(GUI.buttonReleased(materials.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) materials.get(i));
				}
				else if(GUI.buttonReleased(materials.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) materials.get(i));
			}
		}
		else if(selectedType == DataType.Shader)
		{
			List<Shader> shaders = Shader.getShaders();
			for(i = 0; i < shaders.size(); i++)
			{
				if(selected != null)
				{
					if(shaders.get(i).equals(selected))
					{
						if(GUI.buttonReleased(shaders.get(i).name, new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) shaders.get(i));
					}
					else if(GUI.buttonReleased(shaders.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) shaders.get(i));
				}
				else if(GUI.buttonReleased(shaders.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) shaders.get(i));
			}
		}
		else if(selectedType == DataType.Skin)
		{
			List<GUISkin> skins = GUISkin.getSkins();
			for(i = 0; i < skins.size(); i++)
			{
				if(selected != null)
				{
					if(skins.get(i).equals(selected))
					{
						if(GUI.buttonReleased(skins.get(i).getName(), new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) skins.get(i));
					}
					else if(GUI.buttonReleased(skins.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) skins.get(i));
				}
				else if(GUI.buttonReleased(skins.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) skins.get(i));
			}
		}
		else if(selectedType == DataType.Sprite)
		{
			List<Sprite> sprites = Sprite.getSprites();
			for(i = 0; i < sprites.size(); i++)
			{
				if(selected != null)
				{
					if(sprites.get(i).equals(selected))
					{
						if(GUI.buttonReleased(sprites.get(i).name, new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) sprites.get(i));
					}
					else if(GUI.buttonReleased(sprites.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) sprites.get(i));
				}
				else if(GUI.buttonReleased(sprites.get(i).name, new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) sprites.get(i));
			}
		}
		else if(selectedType == DataType.Texture)
		{
			List<Texture> textures = Texture.getTextureInstances();
			for(i = 0; i < textures.size(); i++)
			{
				if(selected != null)
				{
					if(textures.get(i).equals(selected))
					{
						if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) textures.get(i));
					}
					else if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) textures.get(i));
				}
				else if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) textures.get(i));
			}
		}
	}
}
