package com.bruce3x.neumorphism

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils

class NeumorphismLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF()

    init {
        setWillNotDraw(false)
        paint.maskFilter = BlurMaskFilter(60F, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0 until childCount) {
            drawShadow(getChildAt(i), canvas)
        }
        super.onDraw(canvas)
    }

    private fun drawShadow(child: View, canvas: Canvas) {
        val bg = child.background as? GradientDrawable ?: return
        val radius = bg.cornerRadius
        val color = bg.color?.defaultColor!!

        val lp = child.layoutParams as LayoutParams

        paint.maskFilter = if (lp.blur > 0) BlurMaskFilter(lp.blur, BlurMaskFilter.Blur.NORMAL) else null
        bounds.set(
            child.left.toFloat(),
            child.top.toFloat(),
            child.right.toFloat(),
            child.bottom.toFloat()
        )

        bounds.offset(lp.distance, lp.distance)
        paint.color = ColorUtils.blendARGB(color, Color.BLACK, lp.intensity)
        canvas.drawRoundRect(bounds, radius, radius, paint)

        bounds.offset(-2 * lp.distance, -2 * lp.distance)
        paint.color = ColorUtils.blendARGB(color, Color.WHITE, lp.intensity)
        canvas.drawRoundRect(bounds, radius, radius, paint)
    }

    private fun Int.colorPlus(n: Number): Int {
        val r = (Color.red(this) + n.toInt()).coerceIn(0, 255)
        val g = (Color.green(this) + n.toInt()).coerceIn(0, 255)
        val b = (Color.blue(this) + n.toInt()).coerceIn(0, 255)
        return Color.rgb(r, g, b)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = LayoutParams(context, attrs)

    class LayoutParams : FrameLayout.LayoutParams {
        var distance = 0F
        var intensity = 0F
        var blur = 0F

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val ta = c.obtainStyledAttributes(attrs, R.styleable.NeumorphismLayout_Layout)
            distance = ta.getDimensionPixelSize(R.styleable.NeumorphismLayout_Layout_layout_neu_distance, 0).toFloat()
            intensity = ta.getFloat(R.styleable.NeumorphismLayout_Layout_layout_neu_intensity, 0F).coerceIn(0F, 1F)
            blur = ta.getDimensionPixelSize(R.styleable.NeumorphismLayout_Layout_layout_neu_blur, 0).toFloat()
            ta.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, gravity: Int) : super(width, height, gravity)
        constructor(source: ViewGroup.LayoutParams) : super(source)
        constructor(source: MarginLayoutParams) : super(source)
        constructor(source: FrameLayout.LayoutParams) : super(source)
    }
}