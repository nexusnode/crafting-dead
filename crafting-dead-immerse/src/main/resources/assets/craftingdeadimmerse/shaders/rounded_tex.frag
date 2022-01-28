#version 430 compatibility

precision highp float;

layout(location = 0) uniform vec4 u_Radius;
layout(location = 1) uniform vec2 u_Position;
layout(location = 2) uniform vec2 u_Size;
layout(location = 3) uniform sampler2D u_Sampler;

in vec2 f_VertexPosition;
in vec2 f_TexCoord;

out vec4 fragColor;

// from http://www.iquilezles.org/www/articles/distfunctions/distfunctions.htm
// additional thanks to iq for optimizing my conditional block for individual corner radii!
float roundedBoxSDF(vec2 CenterPosition, vec2 Size, vec4 Radius) {
    Radius.xy = (CenterPosition.x > 0.0) ? Radius.xy : Radius.zw;
    Radius.x = (CenterPosition.y > 0.0) ? Radius.x  : Radius.y;
    
    vec2 q = abs(CenterPosition)-Size+Radius.x;
    return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - Radius.x;
}

void main() {
    // How soft the edges should be (in pixels). Higher values could be used to simulate a drop shadow.
    float edgeSoftness  = 0.25f;

    // Calculate distance to edge.   
    float distance = roundedBoxSDF(  f_VertexPosition - u_Position - u_Size / 2.0f, u_Size / 2.0f, u_Radius);

    // Smooth the result (free antialiasing).
    float smoothedAlpha =  1.0f - smoothstep(0.0f, edgeSoftness, distance);

    fragColor = texture(u_Sampler, f_TexCoord) * vec4(gl_Color.xyz, smoothedAlpha * gl_Color.w);
}