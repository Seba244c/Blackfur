package dk.sebsa.blackfur.editor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import dk.sebsa.blackfur.engine.Application;
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
	private int scroll1 = 0;
	private int scroll2 = 0;
	
	public ProjectPanel()
	{
		box = Editor.skin.getStyle("Box");
	}
	
	public void renderTypes(Rect r)
	{
		DataType[] values = DataType.values();
		scroll1 = GUI.setScrollView(DataType.values().length * 26, scroll1);
		for(int i = 0; i < values.length; i++)
		{
			temp.set(0, i * 26, r.width, 26);
			if(!values[i].equals(selectedType))
			{
				if(GUI.buttonPressed(values[i].name() + "s", temp, empty, empty)) selectedType = values[i];
			}
			else
			{
				GUI.buttonPressed(values[i].name() + "s", temp, box, box);
				//Rect s = GUI.box(temp, box);
				//GUI.beginArea(s);
				//GUI.label(values[i].name() + "s", 0, 0);
				//GUI.endArea();
			}
		}
	}
	
	public void renderAssets(Rect r)
	{
		Object selected = Editor.getSelectedAsset();
		
		if(selectedType == DataType.Font)
		{
			List<Font> fonts = Font.getFonts();
			scroll2 = GUI.setScrollView(fonts.size() * 26, scroll2);
			for(i = 0; i < fonts.size(); i++)
			{
				if(selected != null)
				{
					if(fonts.get(i).equals(selected))
					{
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Font/" + fonts.get(i).getName() + ".ttf");
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
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
			scroll2 = GUI.setScrollView(materials.size() * 26, scroll2);
			for(i = 0; i < materials.size(); i++)
			{
				if(selected != null)
				{
					if(materials.get(i).equals(selected))
					{
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Materials/" + materials.get(i).name + ".bfm");
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
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
			scroll2 = GUI.setScrollView(shaders.size() * 26, scroll2);
			for(i = 0; i < shaders.size(); i++)
			{
				if(selected != null)
				{
					if(shaders.get(i).equals(selected))
					{
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Shaders/" + shaders.get(i).name + ".glsl");
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
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
			scroll2 = GUI.setScrollView(skins.size() * 26, scroll2);
			for(i = 0; i < skins.size(); i++)
			{
				if(selected != null)
				{
					if(skins.get(i).equals(selected))
					{
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Skins/" + skins.get(i).getName() + ".bfo");
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
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
			scroll2 = GUI.setScrollView(sprites.size() * 26, scroll2);
			for(i = 0; i < sprites.size(); i++)
			{
				if(selected != null)
				{
					if(sprites.get(i).equals(selected))
					{
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Sprites/" + sprites.get(i).name + ".bfs");
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
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
			scroll2 = GUI.setScrollView(textures.size() * 26, scroll2);
			for(i = 0; i < textures.size(); i++)
			{
				if(selected != null)
				{
					if(textures.get(i).equals(selected))
					{	
						// Open file i Double clicked
						if(Application.input.mouseMultiClicked()) {
							File f = new File(Editor.getProjectDir() + "Textures/" + textures.get(i).getName());
							if(f.exists()) {
								try { Desktop.getDesktop().open(f); } catch (IOException e) {e.printStackTrace(); }
							}
						}
						// Button
						if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), box, box)) Editor.setSelectedAsset((Object) textures.get(i));
					}
					else if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) textures.get(i));
				}
				else if(GUI.buttonReleased(textures.get(i).getName(), new Rect(0, i * 26, r.width, 26), empty, empty)) Editor.setSelectedAsset((Object) textures.get(i));
			}
		}
	}
}
