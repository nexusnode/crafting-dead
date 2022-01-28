#version 430 compatibility

in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 f_VertexColor;
out vec2 f_VertexPosition;

void main() {
    f_VertexPosition = Position.xy;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    f_VertexColor = Color;
}
