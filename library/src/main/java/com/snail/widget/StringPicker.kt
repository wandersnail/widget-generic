package com.snail.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by zeng on 2016/6/19.
 * 滚动选择器
 */
class StringPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var dataList: MutableList<String> = ArrayList()
    /** 选中的位置，这个位置是dataList的中心位置，一直不变  */
    private var currentSelected: Int = 0
    private var paint: Paint? = null
    private var selectedTextSize = -1f
    private var unselectTextSize = -1f
    private var textSpace = -1f
    private var selectedTextColor = -0xafafb0
    private var unselectTextColor = 0x11505050
    private var lastDownY: Float = 0.toFloat()
    /** 滑动的距离  */
    private var moveLen = 0f
    private var isInit = false
    private var selectListener: OnSelectListener? = null
    private var timer: Timer? = null
    private var task: MyTimerTask? = null
    private var gestureDetector: GestureDetector? = null
    private var scroller: Scroller? = null
    private var isFling: Boolean = false
    private var loop: Boolean = false
    private var isEdge: Boolean = false
    private var updateHandler = MyHandler(this)

    /**
     * @return 返回选中的文本，未选中返回null
     */
    val selected: String?
        get() = if (dataList.isEmpty()) null else dataList[currentSelected]

    /**
     * @return 未选中返回-1，否则返回对应索引
     */
    val selectedIndex: Int
        get() = if (dataList.isEmpty()) -1 else currentSelected

    init {
        timer = Timer()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.style = Paint.Style.FILL
        paint!!.textAlign = Paint.Align.CENTER
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                isFling = true
                scroller!!.fling(e2.x.toInt(), e2.y.toInt(), 0, velocityY.toInt(), 0, 0, -2000, 2000)
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
        scroller = Scroller(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (selectedTextSize == -1f || unselectTextSize == -1f) {
            selectedTextSize = height / 4f
            unselectTextSize = selectedTextSize / 3f
        }
        if (textSpace == -1f) textSpace = unselectTextSize * 2.4f
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 根据index绘制view
        if (isInit)
            drawData(canvas)
    }

    override fun computeScroll() {
        if (scroller!!.computeScrollOffset()) {
            if (isFling) doMove(null)
        } else {
            if (isFling) {
                doUp(null)
                isFling = false
            }
        }
    }

    fun setOnSelectListener(listener: OnSelectListener?) {
        selectListener = listener
    }

    fun setTextColor(selectedTextColor: Int, unselectTextColor: Int) {
        this.selectedTextColor = selectedTextColor
        this.unselectTextColor = unselectTextColor
        invalidate()
    }

    fun setTextSize(selectedTextSize: Float, unselectTextSize: Float) {
        this.selectedTextSize = selectedTextSize
        this.unselectTextSize = unselectTextSize
        invalidate()
    }

    /**字体间距，指未选中字体间距 */
    fun setTextSpace(space: Float) {
        textSpace = space
    }

    fun setTypeface(typeface: Typeface) {
        paint!!.typeface = typeface
        invalidate()
    }

    private fun performSelect() {
        if (selectListener != null)
            selectListener!!.onSelect(dataList[currentSelected])
    }

    /**
     * 选择内容是否能循环滚动
     */
    fun setLoopEnable(loop: Boolean) {
        this.loop = loop
    }

    fun setData(dataList: MutableList<String>?) {
        if (dataList == null || dataList.size == 0) return
        this.dataList = dataList
        currentSelected = dataList.size / 2
        invalidate()
    }

    /**
     * 选择选中的item的index
     */
    fun select(item: String) {
        if (!scroller!!.isFinished) {
            scroller!!.abortAnimation()
        }
        if (task != null) {
            task!!.cancel()
            task = null
        }
        for (i in dataList.indices) {
            if (dataList[i] == item) {
                currentSelected = i
                break
            }
        }
        if (loop) {
            val distance = dataList.size / 2 - currentSelected
            if (distance < 0) {
                for (i in 0 until -distance) {
                    moveHeadToTail()
                    currentSelected--
                }
            } else if (distance > 0) {
                for (i in 0 until distance) {
                    moveTailToHead()
                    currentSelected++
                }
            }
        }
        performSelect()
        invalidate()
    }

    private fun drawData(canvas: Canvas) {
        if (dataList.isEmpty()) return
        // 先绘制选中的text再往上往下绘制其余的text
        val scale = parabola(height / 4.0f, moveLen)
        val size = (selectedTextSize - unselectTextSize) * scale + unselectTextSize
        paint!!.textSize = size
        paint!!.color = selectedTextColor
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = width / 2f
        val y = height / 2f + moveLen
        val fmi = paint!!.fontMetricsInt
        val baseline = y - (fmi.bottom / 2f + fmi.top / 2f)

        val indexs = currentSelected
        val textData = dataList[indexs]
        canvas.drawText(textData, x, baseline, paint!!)

        // 绘制上方data
        run {
            var i = 1
            while (currentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (currentSelected + i < dataList.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    /**
     * @param position 距离mCurrentSelected的差值
     * @param type 1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d = textSpace * position + type * moveLen
        val scale = parabola(height / 4.0f, d)
        val size = (selectedTextSize - unselectTextSize) * scale + unselectTextSize
        paint!!.textSize = size
        paint!!.color = getColor(scale)
        val y = (height / 2.0 + type * d).toFloat()
        val fmi = paint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()

        val indexs = currentSelected + type * position
        val textData = dataList[indexs]
        canvas.drawText(textData, (width / 2.0).toFloat(), baseline, paint!!)
    }

    private fun getColor(scale: Float): Int {
        val aa = unselectTextColor and 0x20000000 shr 24 and 0xff
        val ra = unselectTextColor and 0x20000000 shr 16 and 0xff
        val ga = unselectTextColor and 0x20000000 shr 8 and 0xff
        val ba = unselectTextColor and 0x20000000 and 0xff
        val ab = unselectTextColor shr 24 and 0xff
        val rb = unselectTextColor shr 16 and 0xff
        val gb = unselectTextColor shr 8 and 0xff
        val bb = unselectTextColor and 0xff
        val a = (aa + (ab - aa) * scale).toInt()
        val r = (ra + (rb - ra) * scale).toInt()
        val g = (ga + (gb - ga) * scale).toInt()
        val b = (ba + (bb - ba) * scale).toInt()
        return Color.argb(a, r, g, b)
    }

    /**
     * 抛物线
     * @param zero 零点坐标
     * @param x 偏移量
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - Math.pow((x / zero).toDouble(), 2.0)).toFloat()
        return if (f < 0) 0f else f
    }

    private fun moveHeadToTail() {
        if (loop) {
            dataList.add(dataList.removeAt(0))
        } else {
            currentSelected++
        }
    }

    private fun moveTailToHead() {
        if (loop) {
            dataList.add(0, dataList.removeAt(dataList.size - 1))
        } else {
            currentSelected--
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            gestureDetector!!.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> doDown(event)
                MotionEvent.ACTION_MOVE -> doMove(event)
                MotionEvent.ACTION_UP -> doUp(event)
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun doDown(event: MotionEvent) {
        if (!scroller!!.isFinished) {
            scroller!!.abortAnimation()
        }
        if (task != null) {
            task!!.cancel()
            task = null
        }
        lastDownY = event.y
    }

    private fun doMove(event: MotionEvent?) {
        val currY: Float
        if (event == null) {
            currY = scroller!!.currY.toFloat()
        } else {
            currY = event.y
        }
        if (currentSelected >= dataList.size - 1 && currY - lastDownY < 0 || currentSelected <= 0 && currY - lastDownY > 0) {
            if (!loop) {
                if (!scroller!!.isFinished) {
                    scroller!!.abortAnimation()
                }
                if (isEdge) {
                    moveLen = 0f
                } else {
                    moveLen += currY - lastDownY
                    if (moveLen > textSpace / 2) {
                        moveLen -= textSpace
                    } else if (moveLen < -textSpace / 2) {
                        moveLen += textSpace
                    }
                }
                isEdge = true
            } else {
                processMove(currY)
            }
        } else {
            isEdge = false
            processMove(currY)
        }
        lastDownY = currY
        invalidate()
    }

    private fun processMove(currY: Float) {
        moveLen += currY - lastDownY
        if (moveLen > textSpace / 2) {
            // 往下滑超过离开距离
            moveTailToHead()
            moveLen -= textSpace
        } else if (moveLen < -textSpace / 2) {
            // 往上滑超过离开距离
            moveHeadToTail()
            moveLen += textSpace
        }
    }

    private fun doUp(event: MotionEvent?) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(moveLen) < 0.0001) {
            moveLen = 0f
            performSelect()
            return
        }
        if (task != null) {
            task!!.cancel()
            task = null
        }
        task = MyTimerTask(updateHandler)
        timer!!.schedule(task, 0, 10)
    }

    private inner class MyTimerTask internal constructor(internal var handler: Handler) : TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

    interface OnSelectListener {
        fun onSelect(text: String)
    }

    private class MyHandler internal constructor(picker: StringPicker) : Handler() {
        private val weakRef = WeakReference(picker)

        override fun handleMessage(msg: Message) {
            val picker = weakRef.get()
            if (picker != null) {
                if (Math.abs(picker.moveLen) < SPEED) {
                    picker.moveLen = 0f
                    if (picker.task != null) {
                        picker.task!!.cancel()
                        picker.task = null
                        picker.performSelect()
                    }
                } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                    picker.moveLen = picker.moveLen - picker.moveLen / Math.abs(picker.moveLen) * SPEED
                picker.invalidate()
            }
        }
    }

    companion object {
        /** 自动回滚到中间的速度  */
        private const val SPEED = 2f
    }
}