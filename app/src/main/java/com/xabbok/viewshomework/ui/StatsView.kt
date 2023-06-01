package com.xabbok.viewshomework.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import com.xabbok.viewshomework.R
import java.security.InvalidParameterException
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
    private var freeColor: Int = 0
    private var progressAnimator: ValueAnimator? = null
    private var progress: Float = 0F
    private var fillType: Int = 0

    private val startAngle = -90F

    private val statsViewDrawSequentially = StatsViewDrawSequentiallyImpl()
    private val statsViewDrawParallel = StatsViewDrawParallelImpl()
    private val statsViewDrawBidirectional = StatsViewDrawBidirectionalImpl()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)

            colors = resources.getIntArray(getResourceId(R.styleable.StatsView_colors, 0)).toList()

            freeColor = getColor(R.styleable.StatsView_freeColor, Color.LTGRAY)

            fillType = getInt(R.styleable.StatsView_fillType, 1)
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

    private val freePaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = this@StatsView.lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        color = freeColor
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

    var data: StatsViewData?  = StatsViewData(0f, listOf(1f, 1f, 1f, 1f))
        set(value) {
            field = value
            update()
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
        data?.let { data ->
            val realWeights = if (data.freePercent > 0)
                data.weights.map { it * (100 - data.freePercent) / 100 } else data.weights

            canvas.drawCircle(center.x, center.y, radius, freePaint)

            if (data.freePercent < 100) {
                lateinit var drawInterface: StatsViewDrawInterface
                when (fillType) {
                    1 -> {
                        drawInterface = statsViewDrawParallel
                    }

                    2 -> {
                        drawInterface = statsViewDrawSequentially
                    }

                    3 -> {
                        drawInterface = statsViewDrawBidirectional
                    }

                    else -> throw InvalidParameterException("Wrong fillType value!")
                }
                drawInterface.draw(
                    data = data,
                    realWeights = realWeights,
                    canvas = canvas,
                    startAngle = startAngle,
                    animationProgress = progress,
                    paint = paint,
                    colors = colors,
                    oval = oval
                )
            }

            canvas.drawText(
                "%.2f%%".format(realWeights.sum() * 100),
                center.x,
                center.y + textPaint.textSize / 4,
                textPaint
            )
        }
    }

    private fun update() {
        progressAnimator?.let {
            it.removeAllListeners()
            it.cancel()
        }

        progress = 0F

        progressAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener { anim ->
                progress = anim.animatedValue as Float
                invalidate()
            }
            duration = 3000
            interpolator = LinearInterpolator()
        }.also {
            it.start()
        }
    }


}

private fun normalizeData(orig: List<Float>): List<Float> {
    val dataSum = orig.sum()
    val normalData = orig.map {
        it / dataSum
    }
    return normalData
}

object AndroidUtils {
    fun dp(context: Context, dp: Float): Float =
        ceil(context.resources.displayMetrics.density * dp)
}

class StatsViewData constructor(freePercent: Float, weights: List<Float>) {
    var freePercent = freePercent
        set(value) {
            if (value !in 0f..100f)
                throw IllegalArgumentException("freePercent should be 0..100")
            field = value
        }

    var weights = weights
        set(value) {
            field = normalizeData(value)
        }

    init {
        this.freePercent = freePercent
        this.weights = weights
    }
}

fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())