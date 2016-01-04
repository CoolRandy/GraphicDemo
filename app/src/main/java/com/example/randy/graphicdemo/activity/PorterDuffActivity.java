package com.example.randy.graphicdemo.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.example.randy.graphicdemo.R;
import com.example.randy.graphicdemo.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy on 2015/11/21.
 * 这个实例用于实现.Porter-Duff 效果 它包含16条Porter-Duff规则
 * 可以使用上述规则的任意一条控制Paint如何与已有的Canvas图像进行交互
 * 具体会用到Xfermode的子类： PorterDuffXfermode
 */
public class PorterDuffActivity extends SpinnerImageActivity {


    @Override
    protected List<Option> getOptions(Bitmap original) {

        //绘制圆
        Bitmap mask = ImageUtil.createCircle(original.getWidth(), original.getHeight());
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.original), original));
        options.add(new Option(getString(R.string.mask), mask));
        options.add(new Option(getString(R.string.circle_dim_around), circleDimAround(original, mask)));
        options.add(new Option(getString(R.string.clip_circle), drawClipCircle(original)));
        for (PorterDuff.Mode mode: PorterDuff.Mode.values()){
            Log.e("TAG", "mode: " + mode.toString());
            options.add(new Option(mode.toString(), drawWithPorterDuff(original, mask, mode)));
        }
        return options;
    }

    protected int getInitialSelection(int size) {
        return 2;
    }

    private Bitmap drawWithPorterDuff(Bitmap original, Bitmap mask, PorterDuff.Mode mode) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw the original bitmap (DST)
        canvas.drawBitmap(original, 0, 0, null);

        // Draw the mask (SRC) with the specified Porter-Duff mode
        Paint maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(mask, 0, 0, maskPaint);

        return bitmap;
    }

    public Bitmap circleDimAround(Bitmap original, Bitmap mask){

        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //先绘制原始位图，即你要处理的图片 DST
        canvas.drawBitmap(original, 0, 0, null);

        //在采用特定的Porter-Duff模式来绘制mask，也就是轮廓图 SRC  也就是说先用蓝色实行圆嵌到原始位图中，切出圆形区域
        // DST_IN = Whatever was there, keep the part that overlaps with what I'm drawing now
        Paint maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, maskPaint);

        // DST_OVER = Whatever was there, put it over what I'm drawing now
        // 重新将原始位图叠加绘制到前面切出来的圆形位图上，这样就得到了圆形的位图
        Paint overPaint = new Paint();
        overPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        overPaint.setColorFilter(createDimFilter());
        canvas.drawBitmap(original, 0, 0, overPaint);

        return bitmap;
    }

    public Bitmap drawClipCircle(Bitmap original){
        int radius = original.getWidth() / 2;
        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        Path path = new Path();
        path.addCircle(radius, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);//裁剪区域
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;

    }

    public ColorFilter createDimFilter(){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        float scale = 0.5f;
        colorMatrix.setScale(scale, scale, scale, 1f);//rgba  设置亮度  透明度维持不变
        return new ColorMatrixColorFilter(colorMatrix);
    }
}
