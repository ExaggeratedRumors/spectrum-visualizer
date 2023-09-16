package com.vrtools.spectrum_visualizer.model

import com.vrtools.spectrum_visualizer.MainActivity
import com.vrtools.spectrum_visualizer.view.ViewContainer

class RecordingHandler (
    private val activity: MainActivity,
    private val components: ViewContainer
) {
    fun dataChangeNotification(data: IntArray) {
        activity.runOnUiThread {
            components.graph.invalidate(data)
        }
    }
}