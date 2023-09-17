package com.vrtools.spectrum_visualizer.model

import com.vrtools.spectrum_visualizer.MainActivity
import com.vrtools.spectrum_visualizer.view.ViewContainer
import com.vrtools.spectrum_visualizer.utils.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class RecordingRoutine (
    activity: MainActivity,
    val components: ViewContainer
) {
    private val handler = RecordingHandler(activity, components)
    private val recorder = AudioRecorder(activity)

    fun start() {
        recordingService()
    }

    fun release() {
        recordingService()
    }

    fun stop() {
        recorder.stopRecording()
    }

    private fun recordingService() = thread (isDaemon = true) {
        recorder.startRecording()
        while(recorder.isRecording.get()) {
            sleep(RECORD_DELAY)
            if(recorder.readRecordedData()) continue
            val data = recorder.data
                .convertToComplex()
                .fft()
                .divideToThirds()
            handler.dataChangeNotification(getDbSignalForm(data, components.weightingState.get()))
        }
    }
}