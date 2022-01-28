#version 430 compatibility

out vec2 f_VertexPosition;
out vec2 f_TexCoord;

void main() {
	f_VertexPosition = gl_Vertex.xy;
	f_TexCoord = (gl_TextureMatrix[0] * gl_MultiTexCoord0).xy;

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}