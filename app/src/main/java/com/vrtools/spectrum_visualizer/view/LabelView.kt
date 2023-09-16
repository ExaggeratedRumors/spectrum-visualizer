package com.vrtools.spectrum_visualizer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.vrtools.spectrum_visualizer.utils.LABELS_REFRESH_DELAY

class LabelView (context: Context, attrs: AttributeSet?): View(context, attrs) {
    private var counter = 0
    var value = 0

    fun onRefresh(newValue: Int) {
        if(newValue > value || counter == 0) {
            counter = LABELS_REFRESH_DELAY
            value = newValue
        }
        else counter--
    }
}