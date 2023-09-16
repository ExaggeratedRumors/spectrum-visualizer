package com.vrtools.spectrum_visualizer.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.vrtools.spectrum_visualizer.utils.*
import java.util.concurrent.atomic.AtomicBoolean

class GraphView (context: Context, attrs: AttributeSet): View(context, attrs) {
    private var data: IntArray = IntArray(0)
    private val fade = Paint()
    private val paint = Paint()
    private val screen = Rect()
    private val labels = ArrayList<LabelView>()
    private lateinit var bitmap: Bitmap
    private lateinit var lineCanvas: Canvas
    private val isViewInit = AtomicBoolean(false)

    init {
        fade.color = resources.getColor(LABEL_COLOR, null)
        fade.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        for(i in 0..THIRDS_NUMBER) {
            labels.add(LabelView(context, attrs))
        }
    }

    fun initView() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        lineCanvas = Canvas(bitmap)
        isViewInit.set(true)
    }

    fun invalidate(data: IntArray) {
        this.data = data.clone()
        super.invalidate()
    }

    fun changeState() {
        lineCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(!isViewInit.get()) return
        screen.set(0, 0, width, height)
        paint.textSize = 35f
        lineCanvas.drawColor(Color.TRANSPARENT)
        drawComponents(canvas)
        lineCanvas.drawPaint(fade)
        matrix.reset()
        canvas.drawBitmap(bitmap, matrix, null)
    }

    private fun drawComponents(canvas: Canvas) {
        val drawingVector = FloatArray(2 * data.size * 4)
        for(i in data.indices) {
            drawingVector[i * 4] = i * 32f + UPON_LABELS_OFFSET
            drawingVector[i * 4 + 2] = i * 32f + UPON_LABELS_OFFSET
            drawingVector[i * 4 + 1] = height.toFloat() + LOWER_LABELS_OFFSET
            drawingVector[i * 4 + 3] = height.toFloat() + LOWER_LABELS_OFFSET - (data[i] * 12 - 10)
            drawLabel(i, drawingVector, canvas)
        }
        drawLines(drawingVector)
    }

    private fun drawLines(vector: FloatArray) {
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        paint.color = resources.getColor(LINE_COLOR, context.theme)
        lineCanvas.drawLines(vector, paint)
    }

    private fun drawLabel(i: Int, vector: FloatArray, canvas: Canvas) {
        labels[i].onRefresh(data[i])
        var shift = 100
        if(i % 2 == 0) shift += 50
        if(i % 4 == 1 || i % 4 == 0)
            paint.color = Color.GRAY
        else paint.color = Color.WHITE
        canvas.drawText(
            labels[i].value.toString(),
            vector[i * 4] - 7,
            vector[i * 4 + 1] + shift,
            paint
        )
    }
}
