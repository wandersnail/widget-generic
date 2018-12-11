package com.snail.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zeng on 2016/6/19.
 * 滚动选择器
 */
public class StringPicker extends View {
	/** 自动回滚到中间的速度 */
	private static final float SPEED = 2;
	private List<String> dataList = new ArrayList<>();
	/** 选中的位置，这个位置是dataList的中心位置，一直不变 */
	private int currentSelected;
	private Paint paint;
	private float selectedTextSize = -1;
	private float unselectTextSize = -1;
	private float textSpace = -1;
	private int selectedTextColor = 0xFF505050;
	private int unselectTextColor = 0x11505050;
	private int height;
	private int width;
	private float lastDownY;
	/** 滑动的距离 */
	private float moveLen = 0;
	private boolean isInit = false;
	private onSelectListener selectListener;
	private Timer timer;
	private MyTimerTask task;
	private GestureDetector gestureDetector;
	private Scroller scroller;
	private boolean isFling;
	private boolean loop;
	private boolean isEdge;
    private MyHandler updateHandler;

	public StringPicker(Context context) {
		this(context, null);
	}

	public StringPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		timer = new Timer();
        updateHandler = new MyHandler(this);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextAlign(Paint.Align.CENTER);
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				isFling = true;
				scroller.fling((int) e2.getX(), (int) e2.getY(), 0, (int)velocityY, 0, 0, -2000, 2000);
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
		scroller = new Scroller(getContext());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		height = getHeight();
		width = getWidth();
		if (selectedTextSize == -1 || unselectTextSize == -1) {
			selectedTextSize = height / 4f;
			unselectTextSize = selectedTextSize / 3f;
		}
		if (textSpace == -1) textSpace = unselectTextSize * 2.4f;
		isInit = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 根据index绘制view
		if (isInit)
			drawData(canvas);
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			if (isFling) doMove(null);
		} else {
			if (isFling) {
				doUp(null);
				isFling = false;
			}
		}
	}

	public void setOnSelectListener(onSelectListener listener) {
		selectListener = listener;
	}

	public void setTextColor(int selectedTextColor, int unselectTextColor) {
		this.selectedTextColor = selectedTextColor;
		this.unselectTextColor = unselectTextColor;
		invalidate();
	}

	public void setTextSize(float selectedTextSize, float unselectTextSize) {
		this.selectedTextSize = selectedTextSize;
		this.unselectTextSize = unselectTextSize;
		invalidate();
	}

	/**字体间距，指未选中字体间距*/
	public void setTextSpace(float space) {
		textSpace = space;
	}

	public void setTypeface(Typeface typeface) {
		paint.setTypeface(typeface);
		invalidate();
	}

	private void performSelect() {
		if (selectListener != null)
			selectListener.onSelect(dataList.get(currentSelected));
	}

	/**
	 * 选择内容是否能循环滚动
	 */
	public void setLoopEnable(boolean loop) {
		this.loop = loop;
	}

	public void setData(List<String> dataList) {
		if (dataList == null || dataList.size() == 0) return;
		this.dataList = dataList;
		currentSelected = dataList.size() / 2;
		invalidate();
	}

    /**
     * @return 返回选中的文本，未选中返回null
     */
	public String getSelected() {
		return dataList.isEmpty() ? null : dataList.get(currentSelected);
	}

    /**
     * @return 未选中返回-1，否则返回对应索引
     */
	public int getSelectedIndex() {
        return dataList.isEmpty() ? -1 : currentSelected;
    }

	/**
	 * 选择选中的item的index
	 */
	public void select(String item) {
		if(!scroller.isFinished()) {
			scroller.abortAnimation();
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		for (int i = 0; i < dataList.size(); i++) {
			if (dataList.get(i).equals(item)) {
				currentSelected = i;
				break;
			}
		}
		if (loop) {
			int distance = dataList.size() / 2 - currentSelected;
			if (distance < 0) {
				for (int i = 0; i < -distance; i++) {
					moveHeadToTail();
					currentSelected--;
				}
			} else if (distance > 0) {
				for (int i = 0; i < distance; i++) {
					moveTailToHead();
					currentSelected++;
				}
			}
		}
		performSelect();
		invalidate();
	}

	private void drawData(Canvas canvas) {
		if (dataList.isEmpty()) return;
		// 先绘制选中的text再往上往下绘制其余的text
		float scale = parabola(height / 4.0f, moveLen);
		float size = (selectedTextSize - unselectTextSize) * scale + unselectTextSize;
		paint.setTextSize(size);
		paint.setColor(selectedTextColor);
		// text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
		float x = width / 2f;
		float y = height / 2f + moveLen;
		Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
		float baseline = y - (fmi.bottom / 2f + fmi.top / 2f);

		int indexs = currentSelected;
		String textData = dataList.get(indexs);
		canvas.drawText(textData, x, baseline, paint);

		// 绘制上方data
		for (int i = 1; (currentSelected - i) >= 0; i++) {
			drawOtherText(canvas, i, -1);
		}
		// 绘制下方data
		for (int i = 1; (currentSelected + i) < dataList.size(); i++) {
			drawOtherText(canvas, i, 1);
		}
	}

	/**
	 * @param position 距离mCurrentSelected的差值
	 * @param type 1表示向下绘制，-1表示向上绘制
	 */
	private void drawOtherText(Canvas canvas, int position, int type) {
		float d = textSpace * position + type * moveLen;
		float scale = parabola(height / 4.0f, d);
		float size = (selectedTextSize - unselectTextSize) * scale + unselectTextSize;
		paint.setTextSize(size);
		paint.setColor(getColor(scale));
		float y = (float) (height / 2.0 + type * d);
		Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

		int indexs = currentSelected + type * position;
		String textData = dataList.get(indexs);
		canvas.drawText(textData, (float) (width / 2.0), baseline, paint);
	}

	private int getColor(float scale) {
		int Aa = (unselectTextColor & 0x20000000) >> 24 & 0xff;
		int Ra = (unselectTextColor & 0x20000000) >> 16 & 0xff;
		int Ga = (unselectTextColor & 0x20000000) >> 8 & 0xff;
		int Ba = (unselectTextColor & 0x20000000) & 0xff;
		int Ab = unselectTextColor >> 24 & 0xff;
		int Rb = unselectTextColor >> 16 & 0xff;
		int Gb = unselectTextColor >> 8 & 0xff;
		int Bb = unselectTextColor & 0xff;
		int a = (int) (Aa + (Ab - Aa) * scale);
		int r = (int) (Ra + (Rb - Ra) * scale);
		int g = (int) (Ga + (Gb - Ga) * scale);
		int b = (int) (Ba + (Bb - Ba) * scale);
		return Color.argb(a, r, g, b);
	}

	/**
	 * 抛物线 
	 * @param zero 零点坐标
	 * @param x 偏移量
	 */
	private float parabola(float zero, float x) {
		float f = (float) (1 - Math.pow(x / zero, 2));
		return f < 0 ? 0 : f;
	}

	private void moveHeadToTail() {
		if (loop) {
			dataList.add(dataList.remove(0));
		} else {
			currentSelected++;
		}
	}

	private void moveTailToHead() {
		if (loop) {
			dataList.add(0, dataList.remove(dataList.size() - 1));
		} else {
			currentSelected--;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isEnabled()) {
            gestureDetector.onTouchEvent(event);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    doDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    doMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    doUp(event);
                    break;
            }
            return true;
		}
		return super.onTouchEvent(event);
	}

	private void doDown(MotionEvent event) {
		if(!scroller.isFinished()) {
			scroller.abortAnimation();
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		lastDownY = event.getY();
	}

	private void doMove(MotionEvent event) {
		float currY;
		if (event == null) {
			currY = scroller.getCurrY();
		} else {
			currY = event.getY();
		}
		if ((currentSelected >= dataList.size() - 1 && currY - lastDownY < 0) ||
				(currentSelected <= 0 && currY - lastDownY > 0)) {
			if (!loop) {
				if(!scroller.isFinished()) {
					scroller.abortAnimation();
				}
				if (isEdge) {
					moveLen = 0;
				} else {
					moveLen += currY - lastDownY;
					if (moveLen >  textSpace / 2) {
						moveLen = moveLen - textSpace;
					} else if (moveLen < -textSpace / 2) {
						moveLen = moveLen + textSpace;
					}
				}
				isEdge = true;
			} else {
				processMove(currY);
			}
		} else {
			isEdge = false;
			processMove(currY);
		}
		lastDownY = currY;
		invalidate();
	}

	private void processMove(float currY) {
		moveLen += currY - lastDownY;
		if (moveLen > textSpace / 2) {
			// 往下滑超过离开距离
			moveTailToHead();
			moveLen = moveLen - textSpace;
		} else if (moveLen < -textSpace / 2) {
			// 往上滑超过离开距离
			moveHeadToTail();
			moveLen = moveLen + textSpace;
		}
	}

	private void doUp(MotionEvent event) {
		// 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
		if (Math.abs(moveLen) < 0.0001) {
			moveLen = 0;
			performSelect();
			return;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		task = new MyTimerTask(updateHandler);
		timer.schedule(task, 0, 10);
	}

	private class MyTimerTask extends TimerTask {
		Handler handler;

		MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	public interface onSelectListener {
		void onSelect(String text);
	}

	private static class MyHandler extends Handler {
        private WeakReference<StringPicker> weakRef;

        MyHandler(StringPicker picker) {
            weakRef = new WeakReference<>(picker);
        }

        @Override
		public void handleMessage(Message msg) {
            StringPicker picker = weakRef.get();
            if (picker != null) {
                if (Math.abs(picker.moveLen) < SPEED) {
                    picker.moveLen = 0;
                    if (picker.task != null) {
                        picker.task.cancel();
                        picker.task = null;
                        picker.performSelect();
                    }
                } else
                    // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                    picker.moveLen = picker.moveLen - picker.moveLen / Math.abs(picker.moveLen) * SPEED;
                picker.invalidate();
            }
		}
	}
}