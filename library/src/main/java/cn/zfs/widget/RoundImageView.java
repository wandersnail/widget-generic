package cn.zfs.widget;

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
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 描述: 圆角图片
 * 时间: 2018/9/10 22:09
 * 作者: zengfansheng
 */
public class RoundImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint paint;
    private int cornerRadius = 16;
    private Paint paint2;
    private int needFlags;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_rivCornerRadius, cornerRadius);
            needFlags = ta.getInt(R.styleable.RoundImageView_rivRound, 0x0f);
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
            canvas.drawBitmap(bitmap, 0, 0, paint2);
            bitmap.recycle();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void drawPath(Canvas canvas, int needFlags) {
        if (needFlags != 0) {
            Path path = new Path();
            if ((needFlags & 0x01) == 0x01) {//左上角
                needFlags &= 0xfe;
                path.moveTo(0, cornerRadius);
                path.lineTo(0, 0);
                path.lineTo(cornerRadius, 0);
                path.arcTo(new RectF(0, 0, cornerRadius * 2, cornerRadius * 2), -90, -90);
            } else if ((needFlags & 0x02) == 0x02) {//左下角
                needFlags &= 0xfd;
                path.moveTo(0, getHeight() - cornerRadius);
                path.lineTo(0, getHeight());
                path.lineTo(cornerRadius, getHeight());
                path.arcTo(new RectF(0, getHeight() - cornerRadius * 2, cornerRadius * 2, getHeight()), 90, 90);
            } else if ((needFlags & 0x04) == 0x04) {//右上角
                needFlags &= 0xfb;
                path.moveTo(getWidth(), cornerRadius);
                path.lineTo(getWidth(), 0);
                path.lineTo(getWidth() - cornerRadius, 0);
                path.arcTo(new RectF(getWidth() - cornerRadius * 2, 0, getWidth(), cornerRadius * 2), -90, 90);
            } else if ((needFlags & 0x08) == 0x08) {//右下角
                needFlags &= 0xf7;
                path.moveTo(getWidth() - cornerRadius, getHeight());
                path.lineTo(getWidth(), getHeight());
                path.lineTo(getWidth(), getHeight() - cornerRadius);
                path.arcTo(new RectF(getWidth() - cornerRadius * 2, getHeight() - cornerRadius * 2, getWidth(), getHeight()), 0, 90);
            }
            path.close();
            canvas.drawPath(path, paint);
            drawPath(canvas, needFlags);
        }
    }
}
