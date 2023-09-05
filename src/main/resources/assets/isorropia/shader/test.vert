#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec3 color;
out vec2 pass_textureCoords;

void main(void){

	pass_textureCoords = textureCoords;
	color = vec3(position.x, position.y, position.z);
}
