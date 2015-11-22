package com.example.randy.graphicdemo.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by randy on 2015/11/21.
 */
public class FourColorView extends ImageView {

    private Bitmap bitmap = null;

    public FourColorView(Context context) {
        super(context);
    }

    public FourColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FourColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if(null == bitmap){

            Bitmap quater = Bitmap.createBitmap(getWidth()/2, getHeight()/2, Bitmap.Config.ARGB_8888);
            Canvas quaterCanvas = new Canvas(quater);
            //将画布缩小为原来的1/2
            quaterCanvas.scale(0.5f, 0.5f);
            //调用超类的onDraw方法绘制画布 先在右上角绘制出原位图
            super.onDraw(quaterCanvas);
            //重新将画布扩大为原来的2倍，恢复到原view大小
            quaterCanvas.scale(2, 2);
            //根据原有位图的1/4大小，采用不同的画笔进行绘制，分别为4个位置进行绘制
            createBitmap(quater);
            quater.recycle();

        }
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    /**
     * Create a colorfilter that multiplies the RGB channels by one color,and then adds a second color.
     * LightingColorFilter(int mul, int add)
     * R' = R * mul.R + add.R
     * G' = G * mul.G + add.G
     * B' = B * mul.B + add.B
     *
     * Little brother of ColorMatrixColorFilter
     * [ mul.R,     0,     0, 0, add.R
     *       0, mul.G,     0, 0, add.G,
     *       0,     0, mul.B, 0, add.B,
     *       0,     0,     0, 1,     0 ]
     *       mul.R = Color.red(mul) / 255f
     *       e.g. #ff0000 → 0xff / 255 = 255 / 255 = 1
     * @param quarter
     */
    private void createBitmap(Bitmap quarter){

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        // Top left
        paint.setColorFilter(new LightingColorFilter(Color.RED, 0));
        canvas.drawBitmap(quarter, 0, 0, paint);
        // Top right
        paint.setColorFilter(new LightingColorFilter(Color.YELLOW, 0));
        canvas.drawBitmap(quarter, getWidth() / 2, 0, paint);

        // Bottom left
        paint.setColorFilter(new LightingColorFilter(Color.BLUE, 0));
        canvas.drawBitmap(quarter, 0, getHeight()/2, paint);

        // Bottom right
        paint.setColorFilter(new LightingColorFilter(Color.GREEN, 0));
        canvas.drawBitmap(quarter, getWidth() / 2, getHeight() / 2, paint);
    }

    private ColorFilter createColorFilter(float red, float green, float blue) {
        float alpha = 1f;
        float[] transform =
                { red, 0, 0, 0, 0,
                        0, green, 0, 0, 0,
                        0, 0, blue, 0, 0,
                        0, 0, 0, alpha, 0 };
        return new ColorMatrixColorFilter(transform);
    }
}
