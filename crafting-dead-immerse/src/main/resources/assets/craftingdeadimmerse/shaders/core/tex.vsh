#version 430 compatibility

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 f_VertexPosition;
out vec2 f_TexCoord;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

	f_VertexPosition = Position.xy;
	f_TexCoord = UV0;
}