#version 430 compatibility

out vec2 f_VertexPosition;

void main() {
	f_VertexPosition = gl_Vertex.xy;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}