package com.universe.explorer.utils

/**
 * Mathematical utilities for 3D graphics and celestial calculations
 */
object MathUtils {

    // Matrix operations
    fun identity4x4(): FloatArray {
        val matrix = FloatArray(16)
        matrix[0] = 1f
        matrix[5] = 1f
        matrix[10] = 1f
        matrix[15] = 1f
        return matrix
    }

    fun translateMatrix(x: Float, y: Float, z: Float): FloatArray {
        val matrix = identity4x4()
        matrix[12] = x
        matrix[13] = y
        matrix[14] = z
        return matrix
    }

    fun scaleMatrix(sx: Float, sy: Float, sz: Float): FloatArray {
        val matrix = identity4x4()
        matrix[0] = sx
        matrix[5] = sy
        matrix[10] = sz
        return matrix
    }

    fun rotateMatrixX(angle: Float): FloatArray {
        val matrix = identity4x4()
        val cos = kotlin.math.cos(angle)
        val sin = kotlin.math.sin(angle)
        matrix[5] = cos
        matrix[6] = sin
        matrix[9] = -sin
        matrix[10] = cos
        return matrix
    }

    fun rotateMatrixY(angle: Float): FloatArray {
        val matrix = identity4x4()
        val cos = kotlin.math.cos(angle)
        val sin = kotlin.math.sin(angle)
        matrix[0] = cos
        matrix[2] = -sin
        matrix[8] = sin
        matrix[10] = cos
        return matrix
    }

    fun rotateMatrixZ(angle: Float): FloatArray {
        val matrix = identity4x4()
        val cos = kotlin.math.cos(angle)
        val sin = kotlin.math.sin(angle)
        matrix[0] = cos
        matrix[1] = sin
        matrix[4] = -sin
        matrix[5] = cos
        return matrix
    }

    // Vector operations
    fun normalize(x: Float, y: Float, z: Float): FloatArray {
        val length = kotlin.math.sqrt(x * x + y * y + z * z)
        return if (length > 0) {
            floatArrayOf(x / length, y / length, z / length)
        } else {
            floatArrayOf(0f, 0f, 0f)
        }
    }

    fun dotProduct(a: FloatArray, b: FloatArray): Float {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
    }

    fun crossProduct(a: FloatArray, b: FloatArray): FloatArray {
        return floatArrayOf(
            a[1] * b[2] - a[2] * b[1],
            a[2] * b[0] - a[0] * b[2],
            a[0] * b[1] - a[1] * b[0]
        )
    }

    // Astronomical calculations
    fun calculateOrbitalPosition(
        orbitalPeriod: Float,
        semiMajorAxis: Float,
        time: Float
    ): FloatArray {
        val meanAnomaly = (2f * Math.PI.toFloat() * time) / orbitalPeriod
        val x = semiMajorAxis * kotlin.math.cos(meanAnomaly)
        val y = 0f
        val z = semiMajorAxis * kotlin.math.sin(meanAnomaly)
        return floatArrayOf(x, y, z)
    }

    fun calculateRotation(
        rotationPeriod: Float,
        time: Float
    ): Float {
        return (2f * Math.PI.toFloat() * time) / rotationPeriod
    }

    fun distanceToCamera(planetDistance: Float, cameraDistance: Float): Float {
        return kotlin.math.sqrt(
            (planetDistance * planetDistance) + (cameraDistance * cameraDistance)
        )
    }
}
