#version 330

in vec3 vPos;
in vec2 texCoords;
out vec2 TexCoords;
void main() {
    gl_Position=vec4(vPos,1);
    TexCoords=texCoords;
}

