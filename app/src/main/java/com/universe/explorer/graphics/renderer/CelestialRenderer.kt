package com.universe.explorer.graphics.renderer

import android.opengl.GLES30
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView

class CelestialRenderer : GLSurfaceView.Renderer {

    private var programHandle: Int = 0
    private var vertexArray: IntArray = IntArray(1)
    private var vertexBuffer: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES30.glClearColor(0.01f, 0.01f, 0.03f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glDepthFunc(GLES30.GL_LEQUAL)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

        initializeShaders()
        initializeGeometry()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        renderPlanets()
        renderStars()
        renderAtmosphere()
    }

    private fun initializeShaders() {
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER_CODE)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE)

        programHandle = GLES30.glCreateProgram().also { program ->
            GLES30.glAttachShader(program, vertexShader)
            GLES30.glAttachShader(program, fragmentShader)
            GLES30.glLinkProgram(program)
        }
    }

    private fun initializeGeometry() {
        GLES30.glGenVertexArrays(1, vertexArray, 0)
        GLES30.glBindVertexArray(vertexArray[0])

        val buffers = IntArray(1)
        GLES30.glGenBuffers(1, buffers, 0)
        vertexBuffer = buffers[0]
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer)
    }

    private fun renderPlanets() {
        GLES30.glUseProgram(programHandle)
    }

    private fun renderStars() {}

    private fun renderAtmosphere() {}

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }

    companion object {
        private const val VERTEX_SHADER_CODE = """
            #version 300 es
            precision highp float;
            layout(location = 0) in vec3 position;
            layout(location = 1) in vec3 normal;
            layout(location = 2) in vec2 texCoord;
            uniform mat4 uModel;
            uniform mat4 uView;
            uniform mat4 uProjection;
            out vec3 vNormal;
            out vec2 vTexCoord;
            out vec3 vFragPos;
            void main() {
                vFragPos = vec3(uModel * vec4(position, 1.0));
                vNormal = mat3(transpose(inverse(uModel))) * normal;
                vTexCoord = texCoord;
                gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
            }
        """

        private const val FRAGMENT_SHADER_CODE = """
            #version 300 es
            precision mediump float;
            in vec3 vNormal;
            in vec2 vTexCoord;
            in vec3 vFragPos;
            uniform sampler2D uTexture;
            uniform vec3 uLightPos;
            uniform vec3 uViewPos;
            uniform float uAmbient;
            out vec4 FragColor;
            void main() {
                vec3 norm = normalize(vNormal);
                vec3 lightDir = normalize(uLightPos - vFragPos);
                float diff = max(dot(norm, lightDir), 0.0);
                vec3 diffuse = diff * texture(uTexture, vTexCoord).rgb;
                vec3 ambient = uAmbient * texture(uTexture, vTexCoord).rgb;
                FragColor = vec4(ambient + diffuse, 1.0);
            }
        """
    }
}
