#version 430 compatibility

precision highp float;

layout(location = 0) uniform vec2 u_Position;
layout(location = 1) uniform vec2 u_Size;
layout(location = 2) uniform float u_OutlineWidth;
layout(location = 3) uniform vec4 u_OutlineColor;

in vec2 f_VertexPosition;

out vec4 fragColor;

void main() {
    vec2 halfSize = u_Size / 2.0f;
    vec2 centre = u_Position + halfSize;
    vec2 dist = abs(f_VertexPosition - centre);
    if (dist.x > halfSize.x - u_OutlineWidth 
        || dist.y > halfSize.y - u_OutlineWidth) {
        fragColor = u_OutlineColor;
    } else {
        fragColor = gl_Color;
    }
}