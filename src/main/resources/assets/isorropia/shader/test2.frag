#version 400 core

in vec2 pass_textureCoords;
in vec3 color;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void){
	out_Color =  vec4(color, 1) + texture(textureSampler, pass_textureCoords);
}