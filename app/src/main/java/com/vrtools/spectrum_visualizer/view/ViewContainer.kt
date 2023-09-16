package com.vrtools.spectrum_visualizer.view

import android.widget.Button
import com.vrtools.spectrum_visualizer.MainActivity
import com.vrtools.spectrum_visualizer.R

class ViewContainer (private val activity: MainActivity) {
    val graph: GraphView = activity.findViewById(R.id.graphView)
    private val aWeighting: Button = activity.findViewById(R.id.aWeighting)
    private val cWeighting: Button = activity.findViewById(R.id.cWeighting)

    init {
        initAWeightingButton()
        initCWeightingButton()
        initGraph()
    }

    private fun initGraph() {
        graph.post {
            graph.initView()
        }
    }

    private fun initAWeightingButton() {
        aWeighting.setOnClickListener {
            activity.runOnUiThread {
                graph.changeState()
            }
        }
        aWeighting.isActivated = true
        aWeighting.isEnabled = false
    }

    private fun initCWeightingButton() {
        cWeighting.setOnClickListener {
            activity.runOnUiThread {
                graph.changeState()
            }
        }
        cWeighting.isActivated = false
        cWeighting.isEnabled = true
    }
}