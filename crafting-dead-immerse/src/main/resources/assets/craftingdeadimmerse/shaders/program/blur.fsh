#version 110

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;

void main() {
    vec4 blurred = vec4(0.0);

    float tAlpha = 0.0;

    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sample0 = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        tAlpha = tAlpha + sample0.a;
        blurred = blurred + sample0;
    }

    gl_FragColor = vec4(blurred.rgb / (Radius * 2.0 + 1.0), tAlpha);
}