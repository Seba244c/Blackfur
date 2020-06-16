#version 330

uniform sampler2D sampler;

in vec4 color;
in vec2 uvCoords;

out vec4 fragColor;

void main()
{
	fragColor = color * texture2D(sampler, uvCoords);
}