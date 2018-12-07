package cn.zfs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by zengfs on 2016/1/13.
 * 圆形进度条，风格有环形和填充。进度条圆环支持颜色渐变
 */
public class CircleProgressBar extends View {
	public static final int COLOR_STYLE_SOLID = 0;
	public static final int COLOR_STYLE_GRADIENT = 1;
	//用于设置渐变色的颜色集
	private int[] colors;
	//用于设置渐变色的开始色
	private int startColor;
	//用于设置渐变色的结束色
	private int endColor;
	//背景圆环的颜色
	private int backgroundCircleColor;
	//圆环进度的颜色
	private int progressCircleColor;
	//中间进度百分比的字符串的颜色
	private int textColor;
	//中间进度百分比的字符串的字体
	private float textSize;
	//背景圆环的宽度
	private float backgroundCircleWidth;
	//进度圆环的宽度
	private float progressCircleWidth;
	//小圆点的半径
	private float dotRadius;
	//最大进度
	private int max;
	//当前进度
	private int progress;
	//是否显示中间的进度
	private boolean isTextVisible;
	//是否画小圆点
	private boolean isDotVisible;
	//是否显示默认圆环
	private boolean isBackgroundCircleVisible;
	//渐变色是否使用颜色数组
	private boolean isArrayColorEnabled;
	//进度条的风格
	private Paint.Style circleStyle;
	//进度环的颜色风格
	private int colorStyle;
	private Paint paint;
	//用于定义的圆弧的形状和大小的界限
	private RectF oval;
	//用于记录上次渐变色的参数
	private float cx, cy;
	//上次渐变色实例
	private SweepGradient sweepGradient;
	//是否实例化过渐变色
	private boolean isInstantiated;
	//进度条帽的形状
	private Paint.Cap cap;
	//进度条开始角度
	private float startAngle;
	private DecimalFormat textFormat = new DecimalFormat("#");

	public CircleProgressBar(Context context) {
		this(context, null);
	}

	public CircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
		isTextVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbTextVisible, true);
		textSize = typedArray.getDimension(R.styleable.CircleProgressBar_cpbTextSize, 24);
		backgroundCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbBackgroundCircleWidth, 5);
		max = typedArray.getInt(R.styleable.CircleProgressBar_cpbMax, 100);
		progress = typedArray.getInt(R.styleable.CircleProgressBar_cpbProgress, 0);
		circleStyle = intToStyle(typedArray.getInt(R.styleable.CircleProgressBar_cpbCircleStyle, 1));
		isDotVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbDotVisible, false);
		isBackgroundCircleVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbBackgroundCircleVisible, true);
		startColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbStartColor, 0xFFC5D534);
		endColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbEndColor, 0xFF52B7A2);
		progressCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbProgressCircleWidth, backgroundCircleWidth);
		backgroundCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbBackgroundCircleColor, 0xFFEFEFEF);
		progressCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbProgressCircleColor, 0xFF90EA13);
		textColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbTextColor, 0xFF90EA13);
		dotRadius = typedArray.getDimension(R.styleable.CircleProgressBar_cpbDotRadius, 0);
		startAngle = typedArray.getFloat(R.styleable.CircleProgressBar_cpbStartAngle, 0);
		cap = intToCap(typedArray.getInt(R.styleable.CircleProgressBar_cpbStrokeCap,0));
		colorStyle = typedArray.getInt(R.styleable.CircleProgressBar_cpbColorStyle, COLOR_STYLE_SOLID);
		typedArray.recycle();
		init();
	}

	private Paint.Cap intToCap(int cap) {
		switch(cap) {
			case 2:
				return Paint.Cap.SQUARE;
			case 1:
				return Paint.Cap.ROUND;
			default:
				return Paint.Cap.BUTT;
		}
	}

	private Paint.Style intToStyle(int style) {
		switch(style) {
			case 2:
				return Paint.Style.FILL_AND_STROKE;
			case 0:
				return Paint.Style.FILL;
			default:
				return Paint.Style.STROKE;
		}
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setShader(null);
		paint.setStyle(circleStyle);
		paint.setStrokeCap(cap);
		//画最外层的大圆环
		float centerX = getWidth() / 2; //获取圆心的x坐标
		dotRadius = dotRadius == 0 ? progressCircleWidth / 2 : dotRadius;
		float radius = centerX - dotRadius - backgroundCircleWidth / 2; //圆环的半径

		if (isBackgroundCircleVisible) {
			paint.setColor(backgroundCircleColor); //设置圆环的颜色
			paint.setStrokeWidth(backgroundCircleWidth); //设置圆环的宽度
			canvas.drawCircle(centerX, centerX, radius, paint); //画出圆环 
		}

		//画进度百分比
		if (isTextVisible) {
			paint.setStrokeWidth(0);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(textColor);
			paint.setTextSize(textSize);
			paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
			String text = textFormat.format(progress * 100f / max) + "%";
			Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
			int baseline = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
			paint.setTextAlign(Paint.Align.CENTER);
			//画出进度百分比
			canvas.drawText(text, getWidth() / 2, baseline, paint);
		}

		//画圆弧 ，画圆环的进度
		canvas.rotate(-180 + startAngle, centerX, centerX);
		paint.setStyle(circleStyle);
		paint.setStrokeWidth(progressCircleWidth); //设置圆环的宽度

		oval.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius);
		SweepGradient shader = null;
		switch (colorStyle) {
			case COLOR_STYLE_SOLID:
				paint.setShader(null);
				paint.setColor(progressCircleColor);  //设置进度的颜色
				break;
			case COLOR_STYLE_GRADIENT:
				//设置环形渐变色
				shader = getShader(centerX, centerX);
				paint.setShader(shader);
				break;
		}
		switch (circleStyle) {
			case STROKE:
				canvas.drawArc(oval, 0, 360f * progress / max, false, paint);  //根据进度画圆弧			
				break;
			case FILL:
			case FILL_AND_STROKE:
				if (progress != 0)
					canvas.drawArc(oval, 0, 360f * progress / max, true, paint);  //根据进度画圆
				break;
		}

		//画圆环上小圆点
		if (isDotVisible) {
			paint.setStyle(Paint.Style.FILL);
			switch (colorStyle) {
				case COLOR_STYLE_SOLID:
					paint.setColor(progressCircleColor);
					break;
				case COLOR_STYLE_GRADIENT:
					if (progress == 0) {
						paint.setShader(null);
						paint.setColor(isArrayColorEnabled ? colors[0] : startColor);
					} else if (progress == max) {
						paint.setShader(null);
						paint.setColor(isArrayColorEnabled ? colors[colors.length - 1] : endColor);
					} else {
						paint.setShader(shader);
					}
					break;
			}
			//小圆点到圆环的半径
			canvas.drawCircle((float) (centerX + radius * Math.cos(Math.toRadians(360f * progress / max))),
					(float) (centerX + radius * Math.sin(Math.toRadians(360f * progress / max))), dotRadius, paint);
		}
	}

	//获取渐变色实例，避免在onDraw中多次实例化
	private SweepGradient getShader(float cx, float cy) {
		if (this.cx != cx || this.cy != cy || !isInstantiated) {
			this.cx = cx;
			this.cy = cy;
			isInstantiated = true;
			if (isArrayColorEnabled) sweepGradient = new SweepGradient(cx, cy, colors, null);
			else sweepGradient = new SweepGradient(cx, cy, startColor, endColor);
		}
		return sweepGradient;
	}

	/**
	 * 设置百分比文本显示格式
	 */
	public void setTextFormat(DecimalFormat format) {
		textFormat = format;
	}

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度，需要同步
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}

	/**
	 * 设置进度条开始角度，0~360
	 */
	public void setStartAngle(float startAngle) {
		if (startAngle > 360) startAngle = 360;
		if (startAngle < 0) startAngle = 0;
		this.startAngle = startAngle;
	}

	/**
	 * 进度条帽的形状
	 */
	public void setStrokeCap(Paint.Cap cap) {
		this.cap = cap;
	}

	/**
	 * 设置进度圆环的颜色风格
	 */
	public void setColorStyle(int colorStyle) {
		this.colorStyle = colorStyle;
	}

	/**
	 * 设置背景圆环的颜色
	 */
	public void setBackgroundCircleColor(int backgroundCircleColor) {
		this.backgroundCircleColor = backgroundCircleColor;
	}

	/**
	 * 设置进度圆环颜色
	 */
	public void setProgressCircleColor(int progressCircleColor) {
		this.progressCircleColor = progressCircleColor;
	}

	/**
	 * 设置进度圆环颜色
	 */
	public void setProgressCircleColor(int[] colors) {
		this.colors = colors;
		isArrayColorEnabled = true;
	}

	/**
	 * 设置进度圆环颜色
	 */
	public void setProgressCircleColor(int startColor, int endColor) {
		this.startColor = startColor;
		this.endColor = endColor;
		isArrayColorEnabled = false;
	}

	/**
	 * 设置进度文本的颜色
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	/**
	 * 设置进度文本的大小
	 */
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	/**
	 * 设置背景圆环的宽度
	 */
	public void setBackgroundCircleWidth(float backgroundCircleWidth) {
		this.backgroundCircleWidth = backgroundCircleWidth;
	}

	/**
	 * 设置进度圆环的宽度
	 */
	public void setProgressCircleWidth(float progressCircleWidth) {
		this.progressCircleWidth = progressCircleWidth;
	}

	/**
	 * 设置小圆点的半径长度
	 */
	public void setDotRadius(float dotRadius) {
		this.dotRadius = dotRadius;
	}

	/**
	 * 设置进度条整体风格
	 */
	public void setCircleStyle(Paint.Style circleStyle) {
		this.circleStyle = circleStyle;
	}

	/**
	 * 设置进度文本是否显示
	 */
	public void setTextVisible(boolean textVisible) {
		isTextVisible = textVisible;
	}

	/**
	 * 设置小圆点是否显示
	 */
	public void setDotVisible(boolean dotVisible) {
		isDotVisible = dotVisible;
	}

	/**
	 * 设置默认圆环是否显示
	 */
	public void setBackgroundCircleVisible(boolean backgroundCircleVisible) {
		isBackgroundCircleVisible = backgroundCircleVisible;
	}
}
