package com.vrtools.spectrum_visualizer;

import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.AUDIO_RECORD_MAX_VALUE;
import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.BASIC_FREQUENCY;
import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.CALIBRATION;
import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.FFT_SIZE;
import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.SAMPLING_RATE;
import static com.vrtools.spectrum_visualizer.utils.EngineUtilsKt.THIRDS_NUMBER;

import com.vrtools.spectrum_visualizer.utils.Complex;

public class ConverterTest {
    static long[] testDivideToThirds(Complex[] complexData) {
        long[] amplitudeData = new long[THIRDS_NUMBER];
        Complex temp = new Complex();
        for (int numberOfTerce = 1, iterator = 0; iterator < complexData.length; iterator++) {
            temp = temp.plus(complexData[iterator]);
            if ((double) (iterator * SAMPLING_RATE / FFT_SIZE) > testCutoffFrequency(33))
                break;
            if ((double) ((iterator + 1) * SAMPLING_RATE / FFT_SIZE) > testCutoffFrequency(numberOfTerce)
                    && (double) (iterator * SAMPLING_RATE / FFT_SIZE) < (double) SAMPLING_RATE / 2) {
                numberOfTerce++;
                iterator--;
                continue;
            }
            if ((Math.pow(temp.getReal(), 2) + Math.pow(temp.getImag(), 2)) > amplitudeData[numberOfTerce - 1])
                amplitudeData[numberOfTerce - 1] = (long) (Math.pow(temp.getReal(), 2) + Math.pow(temp.getImag(), 2));
            temp = new Complex();
        }
        return amplitudeData;
    }

    static double testCutoffFrequency(int numberOfTerce) {
        return BASIC_FREQUENCY * Math.pow(2f ,(2f * numberOfTerce - 1) / 6f);
    }

    public static Complex[] fftTest(Complex[] x) {
        int N = x.length;
        if (N == 1) return new Complex[] { x[0] };
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fftTest(even);

        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fftTest(odd);
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static Complex[] testConvertToComplex(byte[] rawData){
        double temp;
        Complex[] complexData = new Complex[FFT_SIZE];
        for (int iterator = 0 ; iterator < FFT_SIZE ; iterator++) {
            temp = 1d * ((rawData[2 * iterator] & 0xFF) | (rawData[2 * iterator + 1] << 8)) * CALIBRATION / AUDIO_RECORD_MAX_VALUE;
            complexData[iterator] = new Complex(temp, 0);
        }
        return complexData;
    }
}
