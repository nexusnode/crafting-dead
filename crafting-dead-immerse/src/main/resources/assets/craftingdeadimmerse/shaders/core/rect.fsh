#version 150

precision highp float;

uniform vec2 u_Position;
uniform vec2 u_Size;
uniform float u_OutlineWidth;
uniform vec4 u_OutlineColor;

in vec2 f_VertexPosition;
in vec4 f_VertexColor;

out vec4 fragColor;

void main() {
    vec2 halfSize = u_Size / 2.0f;
    vec2 centre = u_Position + halfSize;
    vec2 dist = abs(f_VertexPosition - centre);
    if (dist.x > halfSize.x - u_OutlineWidth 
        || dist.y > halfSize.y - u_OutlineWidth) {
        fragColor = u_OutlineColor;
    } else {
        fragColor = f_VertexColor;
    }
}