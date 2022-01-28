#version 430 compatibility

out vec3 vPos;

void main() {
   vPos = vec3(gl_Vertex.xy, 0.0);
}