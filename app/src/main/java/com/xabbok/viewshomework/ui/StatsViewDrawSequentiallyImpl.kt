package com.xabbok.viewshomework.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class StatsViewDrawSequentiallyImpl : StatsViewDrawInterface {
    override fun draw(
        data: StatsViewData,
        realWeights: List<Float>,
        canvas: Canvas,
        startAngle: Float,
        animationProgress: Float,
        paint: Paint,
        colors: List<Int>,
        oval: RectF
    ) {
        //рисуем дугу
        var startAngleVar = startAngle
        val maxRealValue = (100F - (data.freePercent / 100F)) / 100F
        val maxProgressValue = animationProgress * maxRealValue

        var progressValueCounter = maxProgressValue
        realWeights.forEachIndexed { index, d ->
            if (progressValueCounter < 0)
                return@forEachIndexed
            val chunkProgressValue = max(min(progressValueCounter, d), 0F)
            progressValueCounter -= (d)
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(
                oval,
                startAngleVar,
                360F * chunkProgressValue,
                false,
                paint
            )
            startAngleVar += chunkProgressValue * 360F
        }

        //исправляем концы дуг
        startAngleVar = -90F
        progressValueCounter = maxProgressValue

        realWeights.forEachIndexed { index, d ->
            if (progressValueCounter < 0)
                return@forEachIndexed
            val chunkProgressValue = max(min(progressValueCounter, d), 0F)
            progressValueCounter -= (d)
            paint.color = colors.getOrElse(index) { generateRandomColor() }

            val degree = startAngleVar

            val startX =
                oval.centerX() + oval.width() / 2F * cos(Math.toRadians(degree.toDouble())).toFloat()
            val startY =
                oval.centerY() + oval.width() / 2F * sin(Math.toRadians(degree.toDouble())).toFloat()

            canvas.drawPoint(startX, startY, paint)

            /*canvas.drawArc(
                oval,
                startAngleVar,
                Float.MIN_VALUE,
                false,
                paint
            )*/
            startAngleVar += chunkProgressValue * 360F
        }
    }
}