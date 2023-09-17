package com.vrtools.spectrum_visualizer.view

import android.widget.Button
import com.vrtools.spectrum_visualizer.MainActivity
import com.vrtools.spectrum_visualizer.R
import com.vrtools.spectrum_visualizer.utils.State
import java.util.concurrent.atomic.AtomicReference

class ViewContainer (private val activity: MainActivity) {
    val graph: GraphView = activity.findViewById(R.id.graphView)
    var weightingState: AtomicReference<State> = AtomicReference(State.A_WEIGHTING)
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
                switchState()
            }
        }
        aWeighting.isActivated = weightingState.get() == State.A_WEIGHTING
        aWeighting.isEnabled = weightingState.get() != State.A_WEIGHTING
    }

    private fun initCWeightingButton() {
        cWeighting.setOnClickListener {
            activity.runOnUiThread {
                graph.changeState()
                switchState()
            }
        }
        cWeighting.isActivated = weightingState.get() == State.C_WEIGHTING
        cWeighting.isEnabled = weightingState.get() != State.C_WEIGHTING
    }

    private fun switchState() {
        aWeighting.isEnabled = !aWeighting.isEnabled
        aWeighting.isActivated = !aWeighting.isActivated
        cWeighting.isEnabled = !cWeighting.isEnabled
        cWeighting.isActivated = !cWeighting.isActivated
        weightingState.set(
            if(weightingState.get() == State.A_WEIGHTING) State.C_WEIGHTING
            else State.A_WEIGHTING
        )
    }
}