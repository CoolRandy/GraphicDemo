package com.example.randy.graphicdemo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by randy on 2015/11/21.
 */
public class ImageUtil {

    /**
     * 绘制一个圆
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createCircle(int width, int height){

        //创建画笔，绘制一个蓝色实心圆
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);

        //Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //设置圆的半径
        float radius = Math.min(width, height) * 0.45f;
        canvas.drawCircle(width/2, height/2, radius, paint);
        return bitmap;
    }
}
