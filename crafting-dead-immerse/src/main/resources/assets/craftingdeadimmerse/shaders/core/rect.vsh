#version 430 compatibility

in vec3 Position;
in vec4 Color;

layout(location = 0) uniform mat4 ModelViewMat;
layout(location = 1) uniform mat4 ProjMat;

out vec4 vertexColor;
out vec2 f_Position;

void main() {
    f_Position = Position.xy;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
}
