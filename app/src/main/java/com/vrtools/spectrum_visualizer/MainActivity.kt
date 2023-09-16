package com.vrtools.spectrum_visualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vrtools.spectrum_visualizer.model.*
import com.vrtools.spectrum_visualizer.view.ViewContainer
import com.vrtools.spectrum_visualizer.utils.permissionCheck

class MainActivity : AppCompatActivity() {
    private lateinit var view: ViewContainer
    private lateinit var routine: RecordingRoutine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionCheck(this)
        setContentView(R.layout.activity_main)
        view = ViewContainer(this)
        routine = RecordingRoutine(this, view)
    }

    override fun onStart() {
        super.onStart()
        routine.start()
    }

    override fun onPause() {
        super.onPause()
        routine.stop()
    }

    override fun onResume() {
        super.onResume()
        routine.release()
    }

    override fun onStop() {
        super.onStop()
        routine.stop()
    }
}