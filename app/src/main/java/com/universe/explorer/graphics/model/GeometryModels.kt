package com.universe.explorer.graphics.model

import java.nio.FloatBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Sphere(
    radius: Float = 1f,
    segments: Int = 64,
    rings: Int = 32
) {

    val vertexBuffer: FloatBuffer
    val normalBuffer: FloatBuffer
    val texCoordBuffer: FloatBuffer
    val indexBuffer: FloatBuffer
    val vertexCount: Int

    init {
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val texCoords = mutableListOf<Float>()
        val indices = mutableListOf<Int>()

        for (ring in 0..rings) {
            val phi = PI.toFloat() * ring / rings
            for (seg in 0..segments) {
                val theta = 2 * PI.toFloat() * seg / segments

                val x = sin(phi) * cos(theta) * radius
                val y = cos(phi) * radius
                val z = sin(phi) * sin(theta) * radius

                vertices.addAll(listOf(x, y, z))
                normals.addAll(listOf(x / radius, y / radius, z / radius))

                val s = seg.toFloat() / segments
                val t = ring.toFloat() / rings
                texCoords.addAll(listOf(s, t))
            }
        }

        for (ring in 0 until rings) {
            for (seg in 0 until segments) {
                val a = ring * (segments + 1) + seg
                val b = a + (segments + 1)

                indices.addAll(listOf(a, b, a + 1))
                indices.addAll(listOf(b, b + 1, a + 1))
            }
        }

        vertexCount = indices.size

        vertexBuffer = FloatBuffer.allocate(vertices.size).apply {
            put(vertices.toFloatArray())
            position(0)
        }

        normalBuffer = FloatBuffer.allocate(normals.size).apply {
            put(normals.toFloatArray())
            position(0)
        }

        texCoordBuffer = FloatBuffer.allocate(texCoords.size).apply {
            put(texCoords.toFloatArray())
            position(0)
        }

        indexBuffer = FloatBuffer.allocate(indices.size).apply {
            put(indices.map { it.toFloat() }.toFloatArray())
            position(0)
        }
    }
}

class Ring(
    innerRadius: Float,
    outerRadius: Float,
    segments: Int = 128
) {
    val vertexBuffer: FloatBuffer
    val vertexCount: Int

    init {
        val vertices = mutableListOf<Float>()

        for (seg in 0..segments) {
            val angle = 2 * PI.toFloat() * seg / segments

            val outerX = cos(angle) * outerRadius
            val outerZ = sin(angle) * outerRadius
            vertices.addAll(listOf(outerX, 0f, outerZ))

            val innerX = cos(angle) * innerRadius
            val innerZ = sin(angle) * innerRadius
            vertices.addAll(listOf(innerX, 0f, innerZ))
        }

        vertexCount = vertices.size

        vertexBuffer = FloatBuffer.allocate(vertices.size).apply {
            put(vertices.toFloatArray())
            position(0)
        }
    }
}

class ParticleSystem(maxParticles: Int = 1000) {
    private val positions = MutableList(maxParticles) { FloatArray(3) }
    private val velocities = MutableList(maxParticles) { FloatArray(3) }
    private val lifetimes = FloatArray(maxParticles)
    private val maxAge = 2f

    fun update(deltaTime: Float) {
        for (i in positions.indices) {
            if (lifetimes[i] > 0) {
                positions[i][0] += velocities[i][0] * deltaTime
                positions[i][1] += velocities[i][1] * deltaTime
                positions[i][2] += velocities[i][2] * deltaTime

                lifetimes[i] -= deltaTime
            }
        }
    }

    fun emit(x: Float, y: Float, z: Float, vx: Float, vy: Float, vz: Float) {
        for (i in positions.indices) {
            if (lifetimes[i] <= 0) {
                positions[i][0] = x
                positions[i][1] = y
                positions[i][2] = z
                velocities[i][0] = vx
                velocities[i][1] = vy
                velocities[i][2] = vz
                lifetimes[i] = maxAge
                break
            }
        }
    }
}
