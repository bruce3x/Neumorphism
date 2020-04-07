package com.bruce3x.neumorphism.sample

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.bruce3x.neumorphism.NeumorphismLayout
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        sbDistance.bind { value ->
            vSquare.updateLayoutParams<NeumorphismLayout.LayoutParams> {
                distance = value
            }
        }
        sbIntensity.bind { value ->
            vSquare.updateLayoutParams<NeumorphismLayout.LayoutParams> {
                intensity = value / 10F
            }
        }
        sbBlur.bind { value ->
            vSquare.updateLayoutParams<NeumorphismLayout.LayoutParams> {
                blur = value
            }
        }

        sbSize.bind { value ->
            vSquare.updateLayoutParams<NeumorphismLayout.LayoutParams> {
                width = dip(value)
                height = dip(value)
            }
        }
        sbRadius.bind { value ->
            vSquare.updateLayoutParams<NeumorphismLayout.LayoutParams> {
                (vSquare.background as? GradientDrawable)?.cornerRadius = dip(value).toFloat()
            }
        }

        btnColor.setOnClickListener {
            MaterialColorPickerDialog.Builder(this)
                .setColorShape(ColorShape.SQAURE)
                .setColorListener { color, hex ->
                    updateColor(color)
                }
                .show()
        }

        val lp = vSquare.layoutParams as NeumorphismLayout.LayoutParams
        sbDistance.setProgress(lp.distance)
        sbIntensity.setProgress(lp.intensity * 10F)
        sbBlur.setProgress(lp.blur)

        updateColor(getColor(R.color.material_color))
    }

    private fun updateColor(color: Int) {
        (vSquare.background as? GradientDrawable)?.setColor(color)
        layNeu.setBackgroundColor(color)
        layNeu.invalidate()

        window.statusBarColor = color
    }

    private fun BubbleSeekBar.bind(callback: (Float) -> Unit) {
        onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListenerAdapter() {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                if (fromUser) callback(progressFloat)
            }
        }
    }

    private fun Context.dip(value: Number): Int = (value.toFloat() * resources.displayMetrics.density).roundToInt()
}
