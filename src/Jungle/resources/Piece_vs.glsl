#version 330

in vec3 vPos;
in vec2 texCoords;
out vec2 TexCoords;
uniform vec2 positionShift;
void main() {
    gl_Position=vec4(vPos.x+positionShift.x,vPos.y+positionShift.y,vPos.z,1);
    TexCoords=texCoords;
}

