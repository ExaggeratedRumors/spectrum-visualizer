package com.vrtools.spectrum_visualizer

import com.vrtools.spectrum_visualizer.ConverterTest.*
import com.vrtools.spectrum_visualizer.utils.*
import org.junit.Assert.*
import org.junit.Test
import java.util.Random


class UnitTest {
    @Test
    fun complexValidation() {
        val a = 5.0
        val b = 4.0
        val delta = 1e-8
        val firstComplex = Complex(a,b)
        val secondComplex = Complex(b,a)
        assertEquals(a, firstComplex.real, delta)
        assertEquals(b, firstComplex.imag, delta)
        val diff = firstComplex - secondComplex
        assertEquals(1.0, diff.real, delta)
        assertEquals(-1.0, diff.imag, delta)
        val multiplication = firstComplex * secondComplex
        assertEquals(0.0, multiplication.real, delta)
        assertEquals(41.0, multiplication.imag, delta)
    }

    @Test
    fun `convert to complex validation`() {
        val rnd = Random()
        val data = ByteArray(5000) { (rnd.nextInt() % 200 - 100).toByte() }
        val kotlinConverter = data.convertToComplex()
        val javaConverter = testConvertToComplex(data)
        (kotlinConverter.indices).forEach{ i ->
            assertEquals(kotlinConverter[i].real, javaConverter[i].real, 0.01)
        }
    }

    @Test
    fun fftValidation() {
        val rnd = Random()
        val data = Array(1024) {
            Complex(
                rnd.nextInt() % 10 * rnd.nextDouble(),
                rnd.nextInt() % 10 * rnd.nextDouble()
            )
        }
        val kotlinConverter = data.fft()
        val javaConverter = fftTest(data)
        (kotlinConverter.indices).forEach{ i ->
            assertEquals(kotlinConverter[i].real, javaConverter[i].real, 0.01)
            assertEquals(kotlinConverter[i].imag, javaConverter[i].imag, 0.01)
        }
    }

    @Test
    fun dividingValidation() {
        val rnd = Random()
        val data = Array(1024) {
            Complex(
                rnd.nextInt() % 10 * rnd.nextDouble(),
                rnd.nextInt() % 10 * rnd.nextDouble()
            )
        }
        val kotlinConverter = data.divideToThirds()
        val javaConverter = testDivideToThirds(data)
        (kotlinConverter.indices).forEach{ assertEquals(kotlinConverter[it], javaConverter[it].toInt()) }
    }

    @Test
    fun cutoffValidation() {
        (0..33).forEach {
            assertEquals(cutoffFrequency(it), testCutoffFrequency(it), 0.01)
        }
    }

}