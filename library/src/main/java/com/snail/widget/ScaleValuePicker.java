package com.snail.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 描述: 刻度尺选值器
 * 时间: 2018/8/26 14:59
 * 作者: zengfansheng
 */
public class ScaleValuePicker extends View {   
    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_TOP = 2;
    
    private Paint paint;    
    private int labelHeight;
    private float value;
    private int scales;
    private Builder builder;
    private Bitmap sliderBitmap;
    private RectF bitmapRectF;
    private boolean canMove;
    private float currentX;
    private OnValueUpdateCallback updateCallback;
    private boolean isInitialized;//是否已获得控件大小
    private boolean hasCacheValue;
    
    public ScaleValuePicker(Context context) {
        this(context, null);
    }

    public ScaleValuePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleValuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);        
        bitmapRectF = new RectF();
    }

    public interface OnValueUpdateCallback {
        void onValueUpdate(float value);
    }

    /**
     * 数值变化回调
     */
    public void setOnValueUpdateCallback(OnValueUpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }
    
    /**标签文本格式*/
    public interface TextFormatter {
        String format(float value);
    }
    
    public void setBuilder(@NonNull Builder builder) {
        this.builder = builder;        
        value = builder.min;
        //计算总刻度数，其实是少一个的
        scales = (int) ((builder.max - builder.min) / (builder.twoBigStepDifValue / 10));
        //计算标签高度
        paint.setTypeface(builder.typeface);
        paint.setTextSize(builder.labelSize);
        String text = "0.00";
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        labelHeight = rect.bottom - rect.top;
        //滑块
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), builder.sliderResId);
        if (bitmap != null) {
            //获取这个图片的宽
            int width = bitmap.getWidth();
            //创建操作图片用的matrix对象 
            Matrix matrix = new Matrix();
            //计算宽高缩放率 
            float scaleWidth = builder.sliderWidth / width;
            //缩放图片动作 
            matrix.postScale(scaleWidth, scaleWidth);
            sliderBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.getHeight(), matrix, true);
            bitmap.recycle();            
        }
        currentX = builder.sliderWidth / 2;
        invalidate();
    }

    public Builder getBuilder() {
        return builder;
    }

    /**
     * 获取当前值
     */
    public float getValue() {
        return value;
    }
    
    /**
     * 设置值
     */
    public void setValue(float value) {
        doSetValue(value, true);
    }

    /**
     * 设置值，不触发回调
     */
    public void setValueNoEvent(float value) {
        doSetValue(value, false);
    }
    
    private void doSetValue(float value, boolean nofify) {
        if (builder != null && value >= builder.min && value <= builder.max) {
            if (value < builder.min) {
                value = builder.min;
            } else if (value > builder.max) {
                value = builder.max;
            }
            if (Math.abs(this.value - value) > 0) {
                this.value = value;
                if (isInitialized) {
                    currentX = (value - builder.min) * (getWidth() - builder.sliderWidth) / (builder.max - builder.min) + builder.sliderWidth / 2;
                } else {
                    hasCacheValue = true;
                }
                invalidate();
                if (nofify && updateCallback != null) {
                    updateCallback.onValueUpdate(value);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (builder != null) {
            isInitialized = true;
            if (hasCacheValue) {
                hasCacheValue = false;
                currentX = (value - builder.min) * (getWidth() - builder.sliderWidth) / (builder.max - builder.min) + builder.sliderWidth / 2;
            }
            paint.setStrokeWidth(builder.scaleWidth);
            //画刻度和标签
            float scaleTotalWidth = getWidth() - builder.sliderWidth;//画刻度的总宽度
            //先算开始的横坐标,两边空白滑块宽度的一半
            float startX = builder.sliderWidth / 2;            
            //长刻度顶部纵坐标 = (控件高度-长刻度-刻度与标签间距-标签高度) / 2
            float longScaleStartY = (getHeight() - builder.longScaleLen - builder.labelAndScaleSpace - labelHeight) / 2;
            float shortScaleLen = builder.longScaleLen * builder.shortLongScaleRatio;
            float midScaleLen = (builder.longScaleLen - shortScaleLen) / 2 + shortScaleLen;
            //计算刻度间距
            float space = scaleTotalWidth / scales;
            //计算刻度间代表的值，用来计算当前数值指示线位置
            float unit = (builder.max - builder.min) / scales;
            //刻度基准线Y坐标
            float scaleBaseline;
            switch (builder.scaleAlignment) {
                case ALIGN_TOP:
                    scaleBaseline = longScaleStartY;
                    break;
                case ALIGN_CENTER:
                    scaleBaseline = longScaleStartY + builder.longScaleLen / 2;
                    break;
                default:
                    scaleBaseline = longScaleStartY + builder.longScaleLen;
                    break;
            }
            for (int i = 0; i <= scales; i++) {
                float scaleStartX = startX - builder.scaleWidth / 2 + i * space;
                //计算刻度长度
                float scaleLen;
                float scaleValue = builder.min + unit * i;
                if (i % 10 == 0) {
                    scaleLen = builder.longScaleLen;
                    //画标签
                    String label = getLabel(scaleValue);
                    float textWidth = paint.measureText(label);
                    paint.setColor(builder.labelColor);
                    float textX = scaleStartX - textWidth / 2;
                    if (i == 0) {//第一个标签
                        canvas.drawText(label, textX < 0 ? 0 : textX, longScaleStartY + builder.longScaleLen + builder.labelAndScaleSpace + labelHeight, paint);
                    } else if (i == scales) {//最后一个标签
                        canvas.drawText(label, textX + textWidth > getWidth() ? getWidth() - textWidth : textX, 
                                longScaleStartY + builder.longScaleLen + builder.labelAndScaleSpace + labelHeight, paint);
                    } else {                        
                        canvas.drawText(label, scaleStartX - textWidth / 2, longScaleStartY + builder.longScaleLen + builder.labelAndScaleSpace + labelHeight, paint);
                    }
                } else if (i % 5 == 0) {
                    scaleLen = midScaleLen;                    
                } else {
                    scaleLen = (int) shortScaleLen;
                }
                float startY;
                switch (builder.scaleAlignment) {
                    case ALIGN_TOP:
                        startY = longScaleStartY;
                        break;
                    case ALIGN_CENTER:
                        startY = longScaleStartY + (builder.longScaleLen - scaleLen) / 2;
                        break;
                    default:
                        startY = longScaleStartY + builder.longScaleLen - scaleLen;
                        break;
                }
                paint.setColor(builder.scaleColor);
                canvas.drawLine(scaleStartX, startY, scaleStartX, startY + scaleLen, paint);
            } 
            //画指示线           
            paint.setColor(builder.indicateLineColor);
            if (builder.indicateLineWidth >= 0) {
                paint.setStrokeWidth(builder.indicateLineWidth);
            }
            canvas.drawLine(currentX, longScaleStartY, currentX, longScaleStartY + builder.longScaleLen, paint);
            //画滑块
            if (sliderBitmap != null) {
                bitmapRectF.left = currentX - startX;
                bitmapRectF.top = scaleBaseline - builder.sliderDistanceToScaleBaseLine - sliderBitmap.getHeight();
                bitmapRectF.right = sliderBitmap.getWidth() + bitmapRectF.left;
                bitmapRectF.bottom = sliderBitmap.getHeight() + bitmapRectF.top;
                canvas.drawBitmap(sliderBitmap, bitmapRectF.left, bitmapRectF.top, paint);
            }            
        }
    }

    private String getLabel(float value) {
        if (builder.textFormatter != null) {
            return builder.textFormatter.format(value);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && builder != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    currentX = event.getX();
                    canMove = bitmapRectF.contains(event.getX(), event.getY());
                    if (builder.clickable && event.getX() >= 0 && event.getX() <= getWidth() && event.getY() >= 0 && event.getY() <= getHeight()) {//点击在范围内
                        canMove = true;//如果可点击改变值，那在按下后应该可以继续滑动
                        updateValue();
                    }                  
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = event.getX() - currentX;
                    if (canMove) {
                        currentX += dx;
                        updateValue();
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    private void updateValue() {
        float startX = builder.sliderWidth / 2;
        if (currentX < startX) {
            currentX = startX;
        } else if (currentX > getWidth() - startX) {
            currentX = getWidth() - startX;
        }
        invalidate();
        float value = (currentX - startX) * (builder.max - builder.min) / (getWidth() - builder.sliderWidth) + builder.min;        
        if (value < builder.min) {
            value = builder.min;
        } else if (value > builder.max) {
            value = builder.max;
        }
        if (Math.abs(this.value - value) > 0) {
            this.value = value;
            if (updateCallback != null) {
                updateCallback.onValueUpdate(value);
            }        
        }
    }

    public static class Builder {
        private float min = 0;
        private float max = 100;
        private float twoBigStepDifValue = 10;//两个长刻度之间值的大小
        private int longScaleLen = 60;//长刻度尺寸
        private int labelColor = Color.GRAY;//标签颜色
        private int labelSize = 15;//标签字体大小
        private Typeface typeface;//标签字体
        private int labelAndScaleSpace = 8;//标签与长刻度的间隔
        private int scaleWidth = 2;//刻度线条宽度
        private int scaleColor = Color.GRAY;//刻度线条颜色
        private int sliderResId;//滑块
        private float sliderWidth = 40;//滑块宽度
        private int sliderDistanceToScaleBaseLine = 6;//滑块与刻度基准线的距离，基准线取决于刻度对齐方式
        private float shortLongScaleRatio = 0.6f;//短刻度与长刻度比例，短/长        
        private TextFormatter textFormatter;
        private int scaleAlignment;//刻度对齐方式
        private int indicateLineColor = Color.BLUE;//数值指示线颜色
        private int indicateLineWidth = -1;
        private boolean clickable;

        /** 设置取值范围 */
        public Builder setRange(float min, float max) {
            this.min = min;
            this.max = max;
            return this;
        }

        /** 两个长刻度之间值的大小 */
        public Builder setTwoBigStepDifValue(float twoBigStepDifValue) {
            this.twoBigStepDifValue = twoBigStepDifValue;
            return this;
        }

        /** 长刻度尺寸 */
        public Builder setLongScaleLen(int longScaleLen) {
            this.longScaleLen = longScaleLen;
            return this;
        }

        /** 标签颜色 */
        public Builder setLabelColor(int labelColor) {
            this.labelColor = labelColor;
            return this;
        }

        /** 标签字体大小 */
        public Builder setLabelSize(int labelSize) {
            this.labelSize = labelSize;
            return this;
        }

        /** 标签字体 */
        public Builder setLabelTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        /** 刻度与标签之间间隔 */
        public Builder setLabelAndScaleSpace(int labelAndScaleSpace) {
            this.labelAndScaleSpace = labelAndScaleSpace;
            return this;
        }

        /** 刻度线条宽度 */
        public Builder setScaleWidth(int scaleWidth) {
            this.scaleWidth = scaleWidth;
            return this;
        }

        /** 刻度线条颜色 */
        public Builder setScaleColor(int scaleColor) {
            this.scaleColor = scaleColor;
            return this;
        }

        /**
         * 设置滑块
         * @param distanceToScaleBaseLine 滑块底部与刻度基准线的距离，基准线取决于刻度对齐方式
         */
        public Builder setSlider(@DrawableRes int sliderResId, int width, int distanceToScaleBaseLine) {
            this.sliderResId = sliderResId;
            sliderWidth = width;
            sliderDistanceToScaleBaseLine = distanceToScaleBaseLine;
            return this;
        }

        /** 短刻度与长刻度比例，短/长 */
        public Builder setShortLongScaleRatio(float shortLongScaleRatio) {
            this.shortLongScaleRatio = shortLongScaleRatio;
            return this;
        }

        /**
         * 数值格式
         */
        public Builder setTextFormatter(TextFormatter textFormatter) {
            this.textFormatter = textFormatter;
            return this;
        }

        /**
         * 设置刻度对齐方式
         * @param alignment {@link #ALIGN_BOTTOM}, {@link #ALIGN_CENTER}, {@link #ALIGN_TOP}
         */
        public Builder setScaleAlignment(int alignment) {
            scaleAlignment = alignment;
            return this;
        }

        /**
         * 设置数值指示线颜色
         */
        public Builder setIndicateLineColor(int color) {
            indicateLineColor = color;
            return this;
        }

        /**
         * 设置数值指示线粗细
         */
        public Builder setIndicateLineWidth(int width) {
            indicateLineWidth = width;
            return this;
        }

        /**
         * 设置是否支持点击选中
         */
        public Builder setClickable(boolean b) {
            clickable = b;
            return this;
        }
    }
}
