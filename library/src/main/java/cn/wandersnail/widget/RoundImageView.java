package cn.wandersnail.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 圆角图片
 * <p>
 * date: 2019/8/23 09:40
 * author: zengfansheng
 */
public class RoundImageView extends AppCompatImageView {
    private Paint paint;
    private int cornerRadius = 16;
    private Paint paint2;
    private int needFlags = 0x0f;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_wswCornerRadius, cornerRadius);
            needFlags = ta.getInt(R.styleable.RoundImageView_wswRound, 0x0f);
            ta.recycle();
        }
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        try {
            Canvas canvas2 = new Canvas(bitmap);
            super.draw(canvas2);
            drawPath(canvas2, needFlags);
            canvas.drawBitmap(bitmap, 0f, 0f, paint2);
            bitmap.recycle();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    private void drawPath(Canvas canvas, int needFlags) {
        if (needFlags != 0) {
            Path path = new Path();
            if ((needFlags & 0x01) == 0x01) { //左上角
                needFlags = needFlags & 0xfe;
                path.moveTo(0f, cornerRadius);
                path.lineTo(0f, 0f);
                path.lineTo(cornerRadius, 0f);
                path.arcTo(new RectF(0f, 0f, cornerRadius * 2, cornerRadius * 2), -90f, -90f);
            } else if ((needFlags & 0x02) == 0x02) {//左下角
                needFlags = needFlags & 0xfd;
                path.moveTo(0f, getHeight() - cornerRadius);
                path.lineTo(0f, getHeight());
                path.lineTo(cornerRadius, getHeight());
                path.arcTo(new RectF(0f, getHeight() - cornerRadius * 2, cornerRadius * 2, getHeight()), 90f, 90f);
            } else if ((needFlags & 0x04) == 0x04) {//右上角
                needFlags = needFlags & 0xfb;
                path.moveTo(getWidth(), cornerRadius);
                path.lineTo(getWidth(), 0f);
                path.lineTo(getWidth() - cornerRadius, 0f);
                path.arcTo(new RectF(getWidth() - cornerRadius * 2, 0f, getWidth(), cornerRadius * 2), -90f, 90f);
            } else if ((needFlags & 0x08) == 0x08) {//右下角
                needFlags = needFlags & 0xf7;
                path.moveTo(getWidth() - cornerRadius, getHeight());
                path.lineTo(getWidth(), getHeight());
                path.lineTo(getWidth(), getHeight() - cornerRadius);
                path.arcTo(new RectF(getWidth() - cornerRadius * 2, getHeight() - cornerRadius * 2, getWidth(), getHeight()), 0f, 90f);
            }
            path.close();
            canvas.drawPath(path, paint);
            drawPath(canvas, needFlags);
        }
    }
}
