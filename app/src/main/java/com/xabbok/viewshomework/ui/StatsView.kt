package com.xabbok.viewshomework.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.xabbok.viewshomework.R
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var textSize = AndroidUtils.dp(context, 20F)
    private var lineWidth = AndroidUtils.dp(context, 5F)
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )
        }
    }

    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = this@StatsView.lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = normalizeData(value)
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty())
            return

        var startAngle = -90F

        //рисуем дугу
        data.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, d * 360F, false, paint)
            startAngle += d * 360F
        }

        //исправляем концы дуг
        startAngle = -90F
        data.forEachIndexed { index, d ->
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, Float.MIN_VALUE, false, paint)
            startAngle += d * 360F
        }

        canvas.drawText(
            "%.2f%%".format(data.sum() * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }

    private fun normalizeData(orig: List<Float>): List<Float> {
        val dataSum = orig.sum()
        val normalData = orig.map {
            it / dataSum
        }
        return normalData
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}

object AndroidUtils {
    fun dp(context: Context, dp: Float): Float =
        ceil(context.resources.displayMetrics.density * dp)
}