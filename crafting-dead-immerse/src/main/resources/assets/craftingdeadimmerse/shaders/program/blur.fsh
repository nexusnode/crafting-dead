#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;

out vec4 fragColor;

void main() {
    vec4 blurred = vec4(0.0);

    float tAlpha = 0.0;

    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sample0 = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        tAlpha = tAlpha + sample0.a;
        blurred = blurred + sample0;
    }

    fragColor = vec4(blurred.rgb / (Radius * 2.0 + 1.0), tAlpha);
}