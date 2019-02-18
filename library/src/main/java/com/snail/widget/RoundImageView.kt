package com.snail.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * 描述: 圆角图片
 * 时间: 2018/9/10 22:09
 * 作者: zengfansheng
 */
class RoundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private val paint: Paint
    private var cornerRadius = 16
    private val paint2: Paint
    private var needFlags: Int = 0x0f

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
            cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_rivCornerRadius, cornerRadius)
            needFlags = ta.getInt(R.styleable.RoundImageView_rivRound, 0x0f)
            ta.recycle()
        }
        paint = Paint()
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        paint2 = Paint()
        paint2.xfermode = null
    }

    override fun draw(canvas: Canvas) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        try {
            val canvas2 = Canvas(bitmap)
            super.draw(canvas2)
            drawPath(canvas2, needFlags)
            canvas.drawBitmap(bitmap, 0f, 0f, paint2)
            bitmap.recycle()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    private fun drawPath(canvas: Canvas, needFlags: Int) {
        var flags = needFlags
        if (flags != 0) {
            val path = Path()
            when {
                flags and 0x01 == 0x01 -> { //左上角
                    flags = flags and 0xfe
                    path.moveTo(0f, cornerRadius.toFloat())
                    path.lineTo(0f, 0f)
                    path.lineTo(cornerRadius.toFloat(), 0f)
                    path.arcTo(RectF(0f, 0f, (cornerRadius * 2).toFloat(), (cornerRadius * 2).toFloat()), -90f, -90f)
                }
                flags and 0x02 == 0x02 -> { //左下角
                    flags = flags and 0xfd
                    path.moveTo(0f, (height - cornerRadius).toFloat())
                    path.lineTo(0f, height.toFloat())
                    path.lineTo(cornerRadius.toFloat(), height.toFloat())
                    path.arcTo(RectF(0f, (height - cornerRadius * 2).toFloat(), (cornerRadius * 2).toFloat(), height.toFloat()), 90f, 90f)
                }
                flags and 0x04 == 0x04 -> { //右上角
                    flags = flags and 0xfb
                    path.moveTo(width.toFloat(), cornerRadius.toFloat())
                    path.lineTo(width.toFloat(), 0f)
                    path.lineTo((width - cornerRadius).toFloat(), 0f)
                    path.arcTo(RectF((width - cornerRadius * 2).toFloat(), 0f, width.toFloat(), (cornerRadius * 2).toFloat()), -90f, 90f)
                }
                flags and 0x08 == 0x08 -> { //右下角
                    flags = flags and 0xf7
                    path.moveTo((width - cornerRadius).toFloat(), height.toFloat())
                    path.lineTo(width.toFloat(), height.toFloat())
                    path.lineTo(width.toFloat(), (height - cornerRadius).toFloat())
                    path.arcTo(RectF((width - cornerRadius * 2).toFloat(), (height - cornerRadius * 2).toFloat(), width.toFloat(), height.toFloat()), 0f, 90f)
                }
            }
            path.close()
            canvas.drawPath(path, paint)
            drawPath(canvas, flags)
        }
    }
}
