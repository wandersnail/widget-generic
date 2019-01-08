package com.snail.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.CompoundButton

/**
 * 开关，有过渡动画，可设置颜色
 */
class SwitchButton : CompoundButton {

    private var mThumbDrawable: Drawable? = null
    private var mBackDrawable: Drawable? = null
    private var mBackColor: ColorStateList? = null
    private var mThumbColor: ColorStateList? = null
    private var mThumbRadius: Float = 0.toFloat()
    private var mBackRadius: Float = 0.toFloat()
    private var mThumbMargin: RectF? = null
    private var mThumbRangeRatio: Float = 0.toFloat()
    var animationDuration: Long = 0
    // fade back drawable or color when dragging or animating
    var isFadeBack: Boolean = false
    private var mTintColor: Int = 0
    private var mThumbWidth: Int = 0
    private var mThumbHeight: Int = 0
    private var mBackWidth: Int = 0
    private var mBackHeight: Int = 0

    private var mCurrThumbColor: Int = 0
    private var mCurrBackColor: Int = 0
    private var mNextBackColor: Int = 0
    private var mOnTextColor: Int = 0
    private var mOffTextColor: Int = 0
    private var mCurrentBackDrawable: Drawable? = null
    private var mNextBackDrawable: Drawable? = null
    private var mThumbRectF: RectF? = null
    private var mBackRectF: RectF? = null
    private var mSafeRectF: RectF? = null
    private var mTextOnRectF: RectF? = null
    private var mTextOffRectF: RectF? = null
    private var mPaint: Paint? = null
    // whether using Drawable for thumb or back
    private var mIsThumbUseDrawable: Boolean = false
    private var mIsBackUseDrawable: Boolean = false
    var isDrawDebugRect = false
        set(drawDebugRect) {
            field = drawDebugRect
            invalidate()
        }
    private var mProgressAnimator: ObjectAnimator? = null
    // animation control
    private var progress: Float = 0.toFloat()
        set(progress) {
            var tp = progress
            if (tp > 1) {
                tp = 1f
            } else if (tp < 0) {
                tp = 0f
            }
            field = tp
            invalidate()
        }
    // temp position of thumb when dragging or animating
    private var mPresentThumbRectF: RectF? = null
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()
    private var mLastX: Float = 0.toFloat()
    private var mTouchSlop: Int = 0
    private var mClickTimeout: Int = 0
    private var mRectPaint: Paint? = null
    var textOn: CharSequence? = null
        private set
    var textOff: CharSequence? = null
        private set
    private var mTextPaint: TextPaint? = null
    private var mOnLayout: Layout? = null
    private var mOffLayout: Layout? = null
    private var mTextWidth: Float = 0.toFloat()
    private var mTextHeight: Float = 0.toFloat()
    private var mTextThumbInset: Int = 0
    private var mTextExtra: Int = 0
    private var mTextAdjust: Int = 0
    // FIX #78,#85 : When restoring saved states, setChecked() called by super. So disable
    // animation and event listening when restoring.
    private var mRestoring = false
    private var mReady = false
    private var mCatch = false

    private var mChildOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null


    /**
     * return the status based on position of thumb
     *
     * @return whether checked or not
     */
    private val statusBasedOnPos: Boolean
        get() = progress > 0.5f

    var thumbDrawable: Drawable?
        get() = mThumbDrawable
        set(thumbDrawable) {
            mThumbDrawable = thumbDrawable
            mIsThumbUseDrawable = mThumbDrawable != null
            refreshDrawableState()
            mReady = false
            requestLayout()
            invalidate()
        }

    var backDrawable: Drawable?
        get() = mBackDrawable
        set(backDrawable) {
            mBackDrawable = backDrawable
            mIsBackUseDrawable = mBackDrawable != null
            refreshDrawableState()
            mReady = false
            requestLayout()
            invalidate()
        }

    var backColor: ColorStateList?
        get() = mBackColor
        set(backColor) {
            mBackColor = backColor
            if (mBackColor != null) {
                backDrawable = null
            }
            invalidate()
        }

    var thumbColor: ColorStateList?
        get() = mThumbColor
        set(thumbColor) {
            mThumbColor = thumbColor
            if (mThumbColor != null) {
                thumbDrawable = null
            }
            invalidate()
        }

    // We need to mark "ready" to false since requestLayout may not cause size changed.
    var thumbRangeRatio: Float
        get() = mThumbRangeRatio
        set(thumbRangeRatio) {
            mThumbRangeRatio = thumbRangeRatio
            mReady = false
            requestLayout()
        }

    var thumbMargin: RectF?
        get() = mThumbMargin
        set(thumbMargin) = if (thumbMargin == null) {
            setThumbMargin(0f, 0f, 0f, 0f)
        } else {
            setThumbMargin(thumbMargin.left, thumbMargin.top, thumbMargin.right, thumbMargin.bottom)
        }

    val thumbWidth: Float
        get() = mThumbWidth.toFloat()

    val thumbHeight: Float
        get() = mThumbHeight.toFloat()

    var thumbRadius: Float
        get() = mThumbRadius
        set(thumbRadius) {
            mThumbRadius = thumbRadius
            if (!mIsThumbUseDrawable) {
                invalidate()
            }
        }

    val backSizeF: PointF
        get() = PointF(mBackRectF!!.width(), mBackRectF!!.height())

    var backRadius: Float
        get() = mBackRadius
        set(backRadius) {
            mBackRadius = backRadius
            if (!mIsBackUseDrawable) {
                invalidate()
            }
        }

    // call this method to refresh color states
    var tintColor: Int
        get() = mTintColor
        set(tintColor) {
            mTintColor = tintColor
            mThumbColor = generateThumbColorWithTintColor(mTintColor)
            mBackColor = generateBackColorWithTintColor(mTintColor)
            mIsBackUseDrawable = false
            mIsThumbUseDrawable = false
            refreshDrawableState()
            invalidate()
        }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    private fun init(attrs: AttributeSet?) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout()

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRectPaint!!.style = Paint.Style.STROKE
        mRectPaint!!.strokeWidth = resources.displayMetrics.density

        mTextPaint = paint

        mThumbRectF = RectF()
        mBackRectF = RectF()
        mSafeRectF = RectF()
        mThumbMargin = RectF()
        mTextOnRectF = RectF()
        mTextOffRectF = RectF()

        mProgressAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 0f).setDuration(DEFAULT_ANIMATION_DURATION.toLong())
        mProgressAnimator!!.interpolator = AccelerateDecelerateInterpolator()

        mPresentThumbRectF = RectF()

        val res = resources
        val density = res.displayMetrics.density

        var thumbDrawable: Drawable? = null
        var thumbColor: ColorStateList? = null
        var margin = density * DEFAULT_THUMB_MARGIN_DP
        var marginLeft = 0f
        var marginRight = 0f
        var marginTop = 0f
        var marginBottom = 0f
        var thumbWidth = 0f
        var thumbHeight = 0f
        var thumbRadius = -1f
        var backRadius = -1f
        var backDrawable: Drawable? = null
        var backColor: ColorStateList? = null
        var thumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO
        var animationDuration = DEFAULT_ANIMATION_DURATION
        var fadeBack = true
        var tintColor = 0
        var textOn: String? = null
        var textOff: String? = null
        var textThumbInset = 0
        var textExtra = 0
        var textAdjust = 0

        var ta: TypedArray? = if (attrs == null) null else context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        if (ta != null) {
            thumbDrawable = ta.getDrawable(R.styleable.SwitchButton_kswThumbDrawable)
            thumbColor = ta.getColorStateList(R.styleable.SwitchButton_kswThumbColor)
            margin = ta.getDimension(R.styleable.SwitchButton_kswThumbMargin, margin)
            marginLeft = ta.getDimension(R.styleable.SwitchButton_kswThumbMarginLeft, margin)
            marginRight = ta.getDimension(R.styleable.SwitchButton_kswThumbMarginRight, margin)
            marginTop = ta.getDimension(R.styleable.SwitchButton_kswThumbMarginTop, margin)
            marginBottom = ta.getDimension(R.styleable.SwitchButton_kswThumbMarginBottom, margin)
            thumbWidth = ta.getDimension(R.styleable.SwitchButton_kswThumbWidth, thumbWidth)
            thumbHeight = ta.getDimension(R.styleable.SwitchButton_kswThumbHeight, thumbHeight)
            thumbRadius = ta.getDimension(R.styleable.SwitchButton_kswThumbRadius, thumbRadius)
            backRadius = ta.getDimension(R.styleable.SwitchButton_kswBackRadius, backRadius)
            backDrawable = ta.getDrawable(R.styleable.SwitchButton_kswBackDrawable)
            backColor = ta.getColorStateList(R.styleable.SwitchButton_kswBackColor)
            thumbRangeRatio = ta.getFloat(R.styleable.SwitchButton_kswThumbRangeRatio, thumbRangeRatio)
            animationDuration = ta.getInteger(R.styleable.SwitchButton_kswAnimationDuration, animationDuration)
            fadeBack = ta.getBoolean(R.styleable.SwitchButton_kswFadeBack, true)
            tintColor = ta.getColor(R.styleable.SwitchButton_kswTintColor, tintColor)
            textOn = ta.getString(R.styleable.SwitchButton_kswTextOn)
            textOff = ta.getString(R.styleable.SwitchButton_kswTextOff)
            textThumbInset = ta.getDimensionPixelSize(R.styleable.SwitchButton_kswTextThumbInset, 0)
            textExtra = ta.getDimensionPixelSize(R.styleable.SwitchButton_kswTextExtra, 0)
            textAdjust = ta.getDimensionPixelSize(R.styleable.SwitchButton_kswTextAdjust, 0)
            ta.recycle()
        }

        // click
        ta = if (attrs == null) null else context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.focusable, android.R.attr.clickable))
        if (ta != null) {
            val focusable = ta.getBoolean(0, true)

            @SuppressLint("ResourceType")
            val clickable = ta.getBoolean(1, focusable)
            isFocusable = focusable
            isClickable = clickable
            ta.recycle()
        } else {
            isFocusable = true
            isClickable = true
        }

        // text
        this.textOn = textOn
        this.textOff = textOff
        mTextThumbInset = textThumbInset
        mTextExtra = textExtra
        mTextAdjust = textAdjust

        // thumb drawable and color
        mThumbDrawable = thumbDrawable
        mThumbColor = thumbColor
        mIsThumbUseDrawable = mThumbDrawable != null
        mTintColor = tintColor
        if (mTintColor == 0) {
            val typedValue = TypedValue()
            var found = false
            try {
                val resId = Class.forName(context.packageName + ".R\$attr").getField("colorAccent").getInt(null)
                if (resId > 0) {
                    found = context.theme.resolveAttribute(resId, typedValue, true)
                }
            } catch (ignored: Exception) {
            }

            mTintColor = if (found) {
                typedValue.data
            } else {
                DEFAULT_TINT_COLOR
            }
        }
        if (!mIsThumbUseDrawable && mThumbColor == null) {
            mThumbColor = generateThumbColorWithTintColor(mTintColor)
            mCurrThumbColor = mThumbColor!!.defaultColor
        }

        // thumbSize
        mThumbWidth = ceil(thumbWidth.toDouble())
        mThumbHeight = ceil(thumbHeight.toDouble())

        // back drawable and color
        mBackDrawable = backDrawable
        mBackColor = backColor
        mIsBackUseDrawable = mBackDrawable != null
        if (!mIsBackUseDrawable && mBackColor == null) {
            mBackColor = generateBackColorWithTintColor(mTintColor)
            mCurrBackColor = mBackColor!!.defaultColor
            mNextBackColor = mBackColor!!.getColorForState(CHECKED_PRESSED_STATE, mCurrBackColor)
        }

        // margin
        mThumbMargin!!.set(marginLeft, marginTop, marginRight, marginBottom)

        // size & measure params must larger than 1
        mThumbRangeRatio = if (mThumbMargin!!.width() >= 0) Math.max(thumbRangeRatio, 1f) else thumbRangeRatio

        mThumbRadius = thumbRadius
        mBackRadius = backRadius
        this.animationDuration = animationDuration.toLong()
        isFadeBack = fadeBack

        mProgressAnimator!!.duration = this.animationDuration

        // sync checked status
        if (isChecked) {
            progress = 1f
        }
    }


    private fun makeLayout(text: CharSequence): Layout {
        return StaticLayout(text, mTextPaint, Math.ceil(Layout.getDesiredWidth(text, mTextPaint).toDouble()).toInt(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /*
         * ensure textLayout
         */
        if (mOnLayout == null && !TextUtils.isEmpty(textOn)) {
            mOnLayout = makeLayout(textOn!!)
        }
        if (mOffLayout == null && !TextUtils.isEmpty(textOff)) {
            mOffLayout = makeLayout(textOff!!)
        }

        val onWidth = (if (mOnLayout != null) mOnLayout!!.width else 0).toFloat()
        val offWidth = (if (mOffLayout != null) mOffLayout!!.width else 0).toFloat()
        mTextWidth = if (onWidth != 0f || offWidth != 0f) {
            Math.max(onWidth, offWidth)
        } else {
            0f
        }

        val onHeight = (if (mOnLayout != null) mOnLayout!!.height else 0).toFloat()
        val offHeight = (if (mOffLayout != null) mOffLayout!!.height else 0).toFloat()
        mTextHeight = if (onHeight != 0f || offHeight != 0f) {
            Math.max(onHeight, offHeight)
        } else {
            0f
        }

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    /**
     * SwitchButton use this formula to determine the final size of thumb, background and itself.
     *
     * textWidth = max(onWidth, offWidth)
     * thumbRange = thumbWidth * rangeRatio
     * textExtraSpace = textWidth + textExtra - (moveRange - thumbWidth + max(thumbMargin.left, thumbMargin.right) + textThumbInset)
     * backWidth = thumbRange + thumbMargin.left + thumbMargin.right + max(textExtraSpace, 0)
     * contentSize = thumbRange + max(thumbMargin.left, 0) + max(thumbMargin.right, 0) + max(textExtraSpace, 0)
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return measuredWidth
     */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var measuredWidth = widthSize

        if (mThumbWidth == 0 && mIsThumbUseDrawable) {
            mThumbWidth = mThumbDrawable!!.intrinsicWidth
        }

        var moveRange: Int
        val textWidth = ceil(mTextWidth.toDouble())
        // how much the background should extend to fit text.
        var textExtraSpace: Int
        var contentSize: Int

        if (mThumbRangeRatio == 0f) {
            mThumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO
        }

        if (widthMode == View.MeasureSpec.EXACTLY) {
            contentSize = widthSize - paddingLeft - paddingRight

            if (mThumbWidth != 0) {
                moveRange = ceil((mThumbWidth * mThumbRangeRatio).toDouble())
                textExtraSpace = textWidth + mTextExtra - (moveRange - mThumbWidth + ceil(Math.max(mThumbMargin!!.left, mThumbMargin!!.right).toDouble()))
                mBackWidth = ceil((moveRange.toFloat() + mThumbMargin!!.left + mThumbMargin!!.right + Math.max(textExtraSpace, 0).toFloat()).toDouble())
                if (mBackWidth < 0) {
                    mThumbWidth = 0
                }
                if (moveRange.toFloat() + Math.max(mThumbMargin!!.left, 0f) + Math.max(mThumbMargin!!.right, 0f) + Math.max(textExtraSpace, 0).toFloat() > contentSize) {
                    mThumbWidth = 0
                }
            }

            if (mThumbWidth == 0) {
                contentSize = widthSize - paddingLeft - paddingRight
                moveRange = ceil((contentSize.toFloat() - Math.max(mThumbMargin!!.left, 0f) - Math.max(mThumbMargin!!.right, 0f)).toDouble())
                if (moveRange < 0) {
                    mThumbWidth = 0
                    mBackWidth = 0
                    return measuredWidth
                }
                mThumbWidth = ceil((moveRange / mThumbRangeRatio).toDouble())
                mBackWidth = ceil((moveRange.toFloat() + mThumbMargin!!.left + mThumbMargin!!.right).toDouble())
                if (mBackWidth < 0) {
                    mThumbWidth = 0
                    mBackWidth = 0
                    return measuredWidth
                }
                textExtraSpace = textWidth + mTextExtra - (moveRange - mThumbWidth + ceil(Math.max(mThumbMargin!!.left, mThumbMargin!!.right).toDouble()))
                if (textExtraSpace > 0) {
                    // since backWidth is determined by view width, so we can only reduce thumbSize.
                    mThumbWidth -= textExtraSpace
                }
                if (mThumbWidth < 0) {
                    mThumbWidth = 0
                    mBackWidth = 0
                    return measuredWidth
                }
            }
        } else {
            /*
            If parent view want SwitchButton to determine it's size itself, we calculate the minimal
            size of it's content. Further more, we ignore the limitation of widthSize since we want
            to display SwitchButton in its actual size rather than compress the shape.
             */
            if (mThumbWidth == 0) {
                /*
                If thumbWidth is not set, use the default one.
                 */
                mThumbWidth = ceil((resources.displayMetrics.density * DEFAULT_THUMB_SIZE_DP).toDouble())
            }
            if (mThumbRangeRatio == 0f) {
                mThumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO
            }

            moveRange = ceil((mThumbWidth * mThumbRangeRatio).toDouble())
            textExtraSpace = ceil((textWidth + mTextExtra - ((moveRange - mThumbWidth).toFloat() + Math.max(mThumbMargin!!.left, mThumbMargin!!.right) + mTextThumbInset.toFloat())).toDouble())
            mBackWidth = ceil((moveRange.toFloat() + mThumbMargin!!.left + mThumbMargin!!.right + Math.max(0, textExtraSpace).toFloat()).toDouble())
            if (mBackWidth < 0) {
                mThumbWidth = 0
                mBackWidth = 0
                return measuredWidth
            }
            contentSize = ceil((moveRange.toFloat() + Math.max(0f, mThumbMargin!!.left) + Math.max(0f, mThumbMargin!!.right) + Math.max(0, textExtraSpace).toFloat()).toDouble())

            measuredWidth = Math.max(contentSize, contentSize + paddingLeft + paddingRight)
        }
        return measuredWidth
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var measuredHeight = heightSize

        if (mThumbHeight == 0 && mIsThumbUseDrawable) {
            mThumbHeight = mThumbDrawable!!.intrinsicHeight
        }
        val contentSize: Int
        val textExtraSpace: Int
        if (heightMode == View.MeasureSpec.EXACTLY) {
            if (mThumbHeight != 0) {
                /*
                If thumbHeight has been set, we calculate backHeight and check if there is enough room.
                 */
                mBackHeight = ceil((mThumbHeight.toFloat() + mThumbMargin!!.top + mThumbMargin!!.bottom).toDouble())
                mBackHeight = ceil(Math.max(mBackHeight.toFloat(), mTextHeight).toDouble())
                if ((mBackHeight + paddingTop + paddingBottom).toFloat() - Math.min(0f, mThumbMargin!!.top) - Math.min(0f, mThumbMargin!!.bottom) > heightSize) {
                    // No enough room, we set thumbHeight to zero to calculate these value again.
                    mThumbHeight = 0
                }
            }

            if (mThumbHeight == 0) {
                mBackHeight = ceil(((heightSize - paddingTop - paddingBottom).toFloat() + Math.min(0f, mThumbMargin!!.top) + Math.min(0f, mThumbMargin!!.bottom)).toDouble())
                if (mBackHeight < 0) {
                    mBackHeight = 0
                    mThumbHeight = 0
                    return measuredHeight
                }
                mThumbHeight = ceil((mBackHeight.toFloat() - mThumbMargin!!.top - mThumbMargin!!.bottom).toDouble())
            }
            if (mThumbHeight < 0) {
                mBackHeight = 0
                mThumbHeight = 0
                return measuredHeight
            }
        } else {
            if (mThumbHeight == 0) {
                mThumbHeight = ceil((resources.displayMetrics.density * DEFAULT_THUMB_SIZE_DP).toDouble())
            }
            mBackHeight = ceil((mThumbHeight.toFloat() + mThumbMargin!!.top + mThumbMargin!!.bottom).toDouble())
            if (mBackHeight < 0) {
                mBackHeight = 0
                mThumbHeight = 0
                return measuredHeight
            }
            textExtraSpace = ceil((mTextHeight - mBackHeight).toDouble())
            if (textExtraSpace > 0) {
                mBackHeight += textExtraSpace
                mThumbHeight += textExtraSpace
            }
            contentSize = Math.max(mThumbHeight, mBackHeight)

            measuredHeight = Math.max(contentSize, contentSize + paddingTop + paddingBottom)
            measuredHeight = Math.max(measuredHeight, suggestedMinimumHeight)
        }

        return measuredHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            setup()
        }
    }

    private fun ceil(dimen: Double): Int {
        return Math.ceil(dimen).toInt()
    }

    /**
     * set up the rect of back and thumb
     */
    private fun setup() {
        if (mThumbWidth == 0 || mThumbHeight == 0 || mBackWidth == 0 || mBackHeight == 0) {
            return
        }

        if (mThumbRadius == -1f) {
            mThumbRadius = (Math.min(mThumbWidth, mThumbHeight) / 2).toFloat()
        }
        if (mBackRadius == -1f) {
            mBackRadius = (Math.min(mBackWidth, mBackHeight) / 2).toFloat()
        }

        val contentWidth = measuredWidth - paddingLeft - paddingRight
        val contentHeight = measuredHeight - paddingTop - paddingBottom

        // max range of drawing content, when thumbMargin is negative, drawing range is larger than backWidth
        val drawingWidth = ceil((mBackWidth.toFloat() - Math.min(0f, mThumbMargin!!.left) - Math.min(0f, mThumbMargin!!.right)).toDouble())
        val drawingHeight = ceil((mBackHeight.toFloat() - Math.min(0f, mThumbMargin!!.top) - Math.min(0f, mThumbMargin!!.bottom)).toDouble())

        val thumbTop: Float
        if (contentHeight <= drawingHeight) {
            thumbTop = paddingTop + Math.max(0f, mThumbMargin!!.top)
        } else {
            // center vertical in content area
            thumbTop = paddingTop.toFloat() + Math.max(0f, mThumbMargin!!.top) + ((contentHeight - drawingHeight + 1) / 2).toFloat()
        }

        val thumbLeft: Float
        if (contentWidth <= mBackWidth) {
            thumbLeft = paddingLeft + Math.max(0f, mThumbMargin!!.left)
        } else {
            thumbLeft = paddingLeft.toFloat() + Math.max(0f, mThumbMargin!!.left) + ((contentWidth - drawingWidth + 1) / 2).toFloat()
        }

        mThumbRectF!!.set(thumbLeft, thumbTop, thumbLeft + mThumbWidth, thumbTop + mThumbHeight)

        val backLeft = mThumbRectF!!.left - mThumbMargin!!.left
        mBackRectF!!.set(backLeft,
                mThumbRectF!!.top - mThumbMargin!!.top,
                backLeft + mBackWidth,
                mThumbRectF!!.top - mThumbMargin!!.top + mBackHeight)

        mSafeRectF!!.set(mThumbRectF!!.left, 0f, mBackRectF!!.right - mThumbMargin!!.right - mThumbRectF!!.width(), 0f)

        val minBackRadius = Math.min(mBackRectF!!.width(), mBackRectF!!.height()) / 2f
        mBackRadius = Math.min(minBackRadius, mBackRadius)

        if (mBackDrawable != null) {
            mBackDrawable!!.setBounds(mBackRectF!!.left.toInt(), mBackRectF!!.top.toInt(), ceil(mBackRectF!!.right.toDouble()), ceil(mBackRectF!!.bottom.toDouble()))
        }

        if (mOnLayout != null) {
            val onLeft = mBackRectF!!.left + (mBackRectF!!.width() + mTextThumbInset - mThumbWidth.toFloat() - mThumbMargin!!.right - mOnLayout!!.width.toFloat()) / 2f - mTextAdjust
            val onTop = mBackRectF!!.top + (mBackRectF!!.height() - mOnLayout!!.height) / 2
            mTextOnRectF!!.set(onLeft, onTop, onLeft + mOnLayout!!.width, onTop + mOnLayout!!.height)
        }

        if (mOffLayout != null) {
            val offLeft = mBackRectF!!.right - (mBackRectF!!.width() + mTextThumbInset - mThumbWidth.toFloat() - mThumbMargin!!.left - mOffLayout!!.width.toFloat()) / 2f - mOffLayout!!.width.toFloat() + mTextAdjust
            val offTop = mBackRectF!!.top + (mBackRectF!!.height() - mOffLayout!!.height) / 2
            mTextOffRectF!!.set(offLeft, offTop, offLeft + mOffLayout!!.width, offTop + mOffLayout!!.height)
        }

        mReady = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!mReady) {
            setup()
        }
        if (!mReady) {
            return
        }

        // fade back
        if (mIsBackUseDrawable) {
            if (isFadeBack && mCurrentBackDrawable != null && mNextBackDrawable != null) {
                // fix #75, 70%A + 30%B != 30%B + 70%A, order matters when mix two layer of different alpha.
                // So make sure the order of on/off layers never change during slide from one endpoint to another.
                val below = if (isChecked) mCurrentBackDrawable else mNextBackDrawable
                val above = if (isChecked) mNextBackDrawable else mCurrentBackDrawable

                var alpha = (255 * progress).toInt()
                below!!.alpha = alpha
                below.draw(canvas)
                alpha = 255 - alpha
                above!!.alpha = alpha
                above.draw(canvas)
            } else {
                mBackDrawable!!.alpha = 255
                mBackDrawable!!.draw(canvas)
            }
        } else {
            if (isFadeBack) {
                var alpha = (255 * progress).toInt()
                var colorAlpha: Int

                // fix #75
                val belowColor = if (isChecked) mCurrBackColor else mNextBackColor
                val aboveColor = if (isChecked) mNextBackColor else mCurrBackColor

                // curr back
                colorAlpha = Color.alpha(belowColor)
                colorAlpha = colorAlpha * alpha / 255
                mPaint!!.setARGB(colorAlpha, Color.red(belowColor), Color.green(belowColor), Color.blue(belowColor))
                canvas.drawRoundRect(mBackRectF!!, mBackRadius, mBackRadius, mPaint!!)

                // next back
                alpha = 255 - alpha
                colorAlpha = Color.alpha(aboveColor)
                colorAlpha = colorAlpha * alpha / 255
                mPaint!!.setARGB(colorAlpha, Color.red(aboveColor), Color.green(aboveColor), Color.blue(aboveColor))
                canvas.drawRoundRect(mBackRectF!!, mBackRadius, mBackRadius, mPaint!!)

                mPaint!!.alpha = 255
            } else {
                mPaint!!.color = mCurrBackColor
                canvas.drawRoundRect(mBackRectF!!, mBackRadius, mBackRadius, mPaint!!)
            }
        }

        // text
        val switchText = if (progress > 0.5) mOnLayout else mOffLayout
        val textRectF = if (progress > 0.5) mTextOnRectF else mTextOffRectF
        if (switchText != null && textRectF != null) {
            val result: Float = if (progress >= 0.75) progress * 4 - 3 else if (progress < 0.25) 1 - progress * 4 else 0f
            val alpha = (255 * result).toInt()
            val textColor = if (progress > 0.5) mOnTextColor else mOffTextColor
            var colorAlpha = Color.alpha(textColor)
            colorAlpha = colorAlpha * alpha / 255
            switchText.paint.setARGB(colorAlpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor))
            canvas.save()
            canvas.translate(textRectF.left, textRectF.top)
            switchText.draw(canvas)
            canvas.restore()
        }

        // thumb
        mPresentThumbRectF!!.set(mThumbRectF)
        mPresentThumbRectF!!.offset(progress * mSafeRectF!!.width(), 0f)
        if (mIsThumbUseDrawable) {
            mThumbDrawable!!.setBounds(mPresentThumbRectF!!.left.toInt(), mPresentThumbRectF!!.top.toInt(), ceil(mPresentThumbRectF!!.right.toDouble()), ceil(mPresentThumbRectF!!.bottom.toDouble()))
            mThumbDrawable!!.draw(canvas)
        } else {
            mPaint!!.color = mCurrThumbColor
            canvas.drawRoundRect(mPresentThumbRectF!!, mThumbRadius, mThumbRadius, mPaint!!)
        }

        if (isDrawDebugRect) {
            mRectPaint!!.color = Color.parseColor("#AA0000")
            canvas.drawRect(mBackRectF!!, mRectPaint!!)
            mRectPaint!!.color = Color.parseColor("#0000FF")
            canvas.drawRect(mPresentThumbRectF!!, mRectPaint!!)
            mRectPaint!!.color = Color.parseColor("#000000")
            canvas.drawLine(mSafeRectF!!.left, mThumbRectF!!.top, mSafeRectF!!.right, mThumbRectF!!.top, mRectPaint!!)
            mRectPaint!!.color = Color.parseColor("#00CC00")
            canvas.drawRect(if (progress > 0.5) mTextOnRectF!! else mTextOffRectF!!, mRectPaint!!)
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        if (!mIsThumbUseDrawable && mThumbColor != null) {
            mCurrThumbColor = mThumbColor!!.getColorForState(drawableState, mCurrThumbColor)
        } else {
            setDrawableState(mThumbDrawable)
        }

        val nextState = if (isChecked) UNCHECKED_PRESSED_STATE else CHECKED_PRESSED_STATE
        val textColors = textColors
        if (textColors != null) {
            val defaultTextColor = textColors.defaultColor
            mOnTextColor = textColors.getColorForState(CHECKED_PRESSED_STATE, defaultTextColor)
            mOffTextColor = textColors.getColorForState(UNCHECKED_PRESSED_STATE, defaultTextColor)
        }
        if (!mIsBackUseDrawable && mBackColor != null) {
            mCurrBackColor = mBackColor!!.getColorForState(drawableState, mCurrBackColor)
            mNextBackColor = mBackColor!!.getColorForState(nextState, mCurrBackColor)
        } else {
            if (mBackDrawable is StateListDrawable && isFadeBack) {
                mBackDrawable!!.state = nextState
                mNextBackDrawable = mBackDrawable!!.current.mutate()
            } else {
                mNextBackDrawable = null
            }
            setDrawableState(mBackDrawable)
            if (mBackDrawable != null) {
                mCurrentBackDrawable = mBackDrawable!!.current.mutate()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (!isEnabled || !isClickable || !isFocusable || !mReady) {
            return false
        }

        val action = event.action

        val deltaX = event.x - mStartX
        val deltaY = event.y - mStartY

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                mLastX = mStartX
                isPressed = true
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                progress += (x - mLastX) / mSafeRectF!!.width()
                if (!mCatch && (Math.abs(deltaX) > mTouchSlop / 2 || Math.abs(deltaY) > mTouchSlop / 2)) {
                    if (deltaY == 0f || Math.abs(deltaX) > Math.abs(deltaY)) {
                        catchView()
                    } else if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        return false
                    }
                }
                mLastX = x
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mCatch = false
                isPressed = false
                val time = (event.eventTime - event.downTime).toFloat()
                if (Math.abs(deltaX) < mTouchSlop && Math.abs(deltaY) < mTouchSlop && time < mClickTimeout) {
                    performClick()
                } else {
                    val nextStatus = statusBasedOnPos
                    if (nextStatus != isChecked) {
                        playSoundEffect(SoundEffectConstants.CLICK)
                        isChecked = nextStatus
                    } else {
                        animateToState(nextStatus)
                    }
                }
            }

            else -> {
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    /**
     * processing animation
     *
     * @param checked checked or unChecked
     */
    protected fun animateToState(checked: Boolean) {
        if (mProgressAnimator == null) {
            return
        }
        if (mProgressAnimator!!.isRunning) {
            mProgressAnimator!!.cancel()
        }
        mProgressAnimator!!.duration = animationDuration
        if (checked) {
            mProgressAnimator!!.setFloatValues(progress, 1f)
        } else {
            mProgressAnimator!!.setFloatValues(progress, 0f)
        }
        mProgressAnimator!!.start()
    }

    private fun catchView() {
        val parent = parent
        parent?.requestDisallowInterceptTouchEvent(true)
        mCatch = true
    }

    override fun setChecked(checked: Boolean) {
        // animate before super.setChecked() become user may call setChecked again in OnCheckedChangedListener
        if (isChecked != checked) {
            animateToState(checked)
        }
        if (mRestoring) {
            setCheckedImmediatelyNoEvent(checked)
        } else {
            super.setChecked(checked)
        }
    }

    fun setCheckedNoEvent(checked: Boolean) {
        if (mChildOnCheckedChangeListener == null) {
            isChecked = checked
        } else {
            super.setOnCheckedChangeListener(null)
            isChecked = checked
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener)
        }
    }

    fun setCheckedImmediatelyNoEvent(checked: Boolean) {
        if (mChildOnCheckedChangeListener == null) {
            setCheckedImmediately(checked)
        } else {
            super.setOnCheckedChangeListener(null)
            setCheckedImmediately(checked)
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener)
        }
    }

    fun toggleNoEvent() {
        if (mChildOnCheckedChangeListener == null) {
            toggle()
        } else {
            super.setOnCheckedChangeListener(null)
            toggle()
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener)
        }
    }

    fun toggleImmediatelyNoEvent() {
        if (mChildOnCheckedChangeListener == null) {
            toggleImmediately()
        } else {
            super.setOnCheckedChangeListener(null)
            toggleImmediately()
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener)
        }
    }

    override fun setOnCheckedChangeListener(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener?) {
        super.setOnCheckedChangeListener(onCheckedChangeListener)
        mChildOnCheckedChangeListener = onCheckedChangeListener
    }

    fun setCheckedImmediately(checked: Boolean) {
        super.setChecked(checked)
        if (mProgressAnimator != null && mProgressAnimator!!.isRunning) {
            mProgressAnimator!!.cancel()
        }
        progress = if (checked) 1f else 0f
        invalidate()
    }

    fun toggleImmediately() {
        setCheckedImmediately(!isChecked)
    }

    private fun setDrawableState(drawable: Drawable?) {
        if (drawable != null) {
            val myDrawableState = drawableState
            drawable.state = myDrawableState
            invalidate()
        }
    }

    fun setThumbDrawableRes(thumbDrawableRes: Int) {
        thumbDrawable = ContextCompat.getDrawable(context, thumbDrawableRes)
    }

    fun setBackDrawableRes(backDrawableRes: Int) {
        backDrawable = ContextCompat.getDrawable(context, backDrawableRes)
    }

    fun setBackColorRes(backColorRes: Int) {
        backColor = ContextCompat.getColorStateList(context, backColorRes)
    }

    fun setThumbColorRes(thumbColorRes: Int) {
        thumbColor = ContextCompat.getColorStateList(context, thumbColorRes)
    }

    fun setThumbMargin(left: Float, top: Float, right: Float, bottom: Float) {
        mThumbMargin!!.set(left, top, right, bottom)
        mReady = false
        requestLayout()
    }

    fun setThumbSize(width: Int, height: Int) {
        mThumbWidth = width
        mThumbHeight = height
        mReady = false
        requestLayout()
    }

    fun setText(onText: CharSequence?, offText: CharSequence?) {
        textOn = onText
        textOff = offText

        mOnLayout = null
        mOffLayout = null

        mReady = false
        requestLayout()
        invalidate()
    }

    fun setTextThumbInset(textThumbInset: Int) {
        mTextThumbInset = textThumbInset
        mReady = false
        requestLayout()
        invalidate()
    }

    fun setTextExtra(textExtra: Int) {
        mTextExtra = textExtra
        mReady = false
        requestLayout()
        invalidate()
    }

    fun setTextAdjust(textAdjust: Int) {
        mTextAdjust = textAdjust
        mReady = false
        requestLayout()
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.onText = textOn
        ss.offText = textOff
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        setText(ss.onText, ss.offText)
        mRestoring = true
        super.onRestoreInstanceState(ss.superState)
        mRestoring = false
    }

    internal class SavedState : View.BaseSavedState {
        var onText: CharSequence? = null
        var offText: CharSequence? = null

        constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            onText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(`in`)
            offText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(`in`)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            TextUtils.writeToParcel(onText, out, flags)
            TextUtils.writeToParcel(offText, out, flags)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        const val ENABLE_ATTR = android.R.attr.state_enabled
        const val CHECKED_ATTR = android.R.attr.state_checked
        const val PRESSED_ATTR = android.R.attr.state_pressed
        const val DEFAULT_THUMB_RANGE_RATIO = 1.8f
        const val DEFAULT_THUMB_SIZE_DP = 20
        const val DEFAULT_THUMB_MARGIN_DP = 2
        const val DEFAULT_ANIMATION_DURATION = 250
        const val DEFAULT_TINT_COLOR = 0x327FC2

        private val CHECKED_PRESSED_STATE = intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed)
        private val UNCHECKED_PRESSED_STATE = intArrayOf(-android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed)

        val defaultThumbColor: ColorStateList
            get() {
                val states = arrayOf(intArrayOf(-SwitchButton.ENABLE_ATTR, SwitchButton.CHECKED_ATTR), intArrayOf(-SwitchButton.ENABLE_ATTR), intArrayOf(SwitchButton.PRESSED_ATTR, -SwitchButton.CHECKED_ATTR), intArrayOf(SwitchButton.PRESSED_ATTR, SwitchButton.CHECKED_ATTR), intArrayOf(SwitchButton.CHECKED_ATTR), intArrayOf(-SwitchButton.CHECKED_ATTR))
                val colors = intArrayOf(-0x1, -0x1, -0x1, -0x1, -0x1, -0x1)
                return ColorStateList(states, colors)
            }

        val defaultBackColor: ColorStateList
            get() {
                val states = arrayOf(intArrayOf(-SwitchButton.ENABLE_ATTR, SwitchButton.CHECKED_ATTR), intArrayOf(-SwitchButton.ENABLE_ATTR), intArrayOf(SwitchButton.PRESSED_ATTR, -SwitchButton.CHECKED_ATTR), intArrayOf(SwitchButton.PRESSED_ATTR, SwitchButton.CHECKED_ATTR), intArrayOf(SwitchButton.CHECKED_ATTR), intArrayOf(-SwitchButton.CHECKED_ATTR))
                val colors = intArrayOf(-0x66bb24a2, -0x333334, -0x333334, -0xbb24a2, -0xbb24a2, -0x333334)
                return ColorStateList(states, colors)
            }

        //改变此处各状态颜色，可以改变滑动块颜色
        fun generateThumbColorWithTintColor(tintColor: Int): ColorStateList {
            val states = arrayOf(intArrayOf(-ENABLE_ATTR, CHECKED_ATTR), intArrayOf(-ENABLE_ATTR), intArrayOf(PRESSED_ATTR, -CHECKED_ATTR), intArrayOf(PRESSED_ATTR, CHECKED_ATTR), intArrayOf(CHECKED_ATTR), intArrayOf(-CHECKED_ATTR))
            val colors = intArrayOf(tintColor - -0x56000000, -0x454546, tintColor - -0x67000000, tintColor - -0x67000000, tintColor or -0x1000000, -0x111112)
            return ColorStateList(states, colors)
        }

        //改变此处各状态颜色，可以改变背景颜色
        fun generateBackColorWithTintColor(tintColor: Int): ColorStateList {
            val states = arrayOf(intArrayOf(-ENABLE_ATTR, CHECKED_ATTR), intArrayOf(-ENABLE_ATTR), intArrayOf(CHECKED_ATTR, PRESSED_ATTR), intArrayOf(-CHECKED_ATTR, PRESSED_ATTR), intArrayOf(CHECKED_ATTR), intArrayOf(-CHECKED_ATTR))
            val colors = intArrayOf(tintColor - -0x1f000000, 0x10000000, tintColor - -0x30000000, 0x20000000, tintColor - -0x30000000, 0x20000000)
            return ColorStateList(states, colors)
        }
    }
}