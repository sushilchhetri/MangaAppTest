package com.mangaversetest.utils.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import com.mangaversetest.R
import kotlin.math.max

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: FaceDetectorResult? = null
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var referencePaint = Paint()

    private var bounds = Rect()

    private var scale = 1f
    private var dx = 0f
    private var dy = 0f
    private var imageWidth = 0
    private var imageHeight = 0

    init {
        initPaints()
    }

    fun clear() {
        results = null
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        referencePaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.purple_500)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE

        referencePaint.color = Color.RED
        referencePaint.style = Paint.Style.STROKE
        referencePaint.strokeWidth = 6f
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // 🔲 Define larger square (60% of screen width), centered
        val referenceBoxSize = width * 0.6f
        val refLeft = (width - referenceBoxSize) / 2
        val refTop = (height - referenceBoxSize) / 2
        val refRight = refLeft + referenceBoxSize
        val refBottom = refTop + referenceBoxSize
        val referenceRect = RectF(refLeft, refTop, refRight, refBottom)

        var faceInsideReference = false

        results?.let {
            for (detection in it.detections()) {
                val boundingBox = detection.boundingBox()

                val left = boundingBox.left * scale + dx
                val top = boundingBox.top * scale + dy
                val right = boundingBox.right * scale + dx
                val bottom = boundingBox.bottom * scale + dy

                val faceRect = RectF(left, top, right, bottom)

                // Calculate the center of the face
                val faceCenterX = (left + right) / 2
                val faceCenterY = (top + bottom) / 2

                // Check if the face center is within the reference box (with a small tolerance)
                if (referenceRect.contains(faceCenterX, faceCenterY)) {
                    faceInsideReference = true
                }

            }
        }

        referencePaint.color = if (faceInsideReference) Color.GREEN else Color.RED
        canvas.drawRect(referenceRect, referencePaint)
    }


    fun setResults(
        detectionResults: FaceDetectorResult,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        results = detectionResults
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight
        scale = max(scaleX, scaleY)

        dx = (width - imageWidth * scale) / 2
        dy = (height - imageHeight * scale) / 2

        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
