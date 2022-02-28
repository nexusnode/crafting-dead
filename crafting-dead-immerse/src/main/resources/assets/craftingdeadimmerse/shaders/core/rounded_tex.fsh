#version 150

precision highp float;

uniform vec4 ColorModulator;
uniform sampler2D Sampler0;

uniform vec4 u_Radius;
uniform vec2 u_Position;
uniform vec2 u_Size;

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

    fragColor = texture(Sampler0, f_TexCoord) * ColorModulator * vec4(1.0, 1.0, 1.0, smoothedAlpha);
}