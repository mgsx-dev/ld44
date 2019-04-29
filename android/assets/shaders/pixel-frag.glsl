#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_scale;

#HEADER

void main() {
    vec2 tc = v_texCoords;

    float dx = u_scale.x;
    float dy = u_scale.y;

    vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
#START
    // color += texture2D(u_texture, vec2(tc.x - dx * 5, tc.y - dy * 2)) * 0.5;
    // color += texture2D(u_texture, vec2(tc.x - dx * 5, tc.y - dy * 2)) * 0.612;
#END

    gl_FragColor = color;
}
