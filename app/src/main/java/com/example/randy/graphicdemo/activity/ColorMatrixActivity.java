package com.example.randy.graphicdemo.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.example.randy.graphicdemo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by randy on 2015/11/21.
 */
public class ColorMatrixActivity extends SpinnerImageActivity {


    @Override
    protected List<Option> getOptions(Bitmap original) {
        return Arrays.asList(

                new Option(getString(R.string.original), original),
                createOption(original, R.string.grayscale, getGreyscaleColorMatrix()),
                createOption(original, R.string.sepia, getSepiaColorMatrix()),
                createOption(original, R.string.binary, getBineryColorMatrix()),
                createOption(original, R.string.invert, new ColorMatrix(new float[]{
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0
                })),
                createOption(original, R.string.alpha_blue, new ColorMatrix(new float[]{
                        0,    0,    0, 0,   0,
                        0.3f,    0,    0, 0,  50,
                        0,    0,    0, 0, 255,
                        0.2f, 0.4f, 0.4f, 0, -30
                })),
                createOption(original, R.string.alpha_pink, new ColorMatrix(new float[]{
                        0,    0,    0, 0, 255,
                        0,    0,    0, 0,   0,
                        0.2f,    0,    0, 0,  50,
                        0.2f, 0.2f, 0.2f, 0, -20
                }))
        );
    }

    /**
     * 根据原始位图，title的id以及ColorMatrix来创建Option对象
     * @param original
     * @param titleId
     * @param colorMatrix
     * @return
     */
    public Option createOption(Bitmap original, int titleId, ColorMatrix colorMatrix){

        String title = getString(titleId);
        Bitmap bitmap = processBitmap(original, colorMatrix);
        return new Option(title, bitmap);
    }

    /**
     * 对位图进行处理
     * @param bitmap
     * @return
     */
    public Bitmap processBitmap(Bitmap bitmap, ColorMatrix colorMatrix){

        //这里调用createBitmap来创建一个可变的bitmap对象  而不是使用原始图片的位图
        Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //根据新创建的位图对象创建画布
        Canvas canvas = new Canvas(bm);
        //创建画笔
        Paint paint = new Paint();
        //为画笔设置过滤器
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //采用画笔将指定的位图绘制在画布上
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bm;
    }
    /**
     * 灰度处理
     * setSaturation(0)对应的矩阵：
     * [ 0.213, 0.715, 0.072, 0, 0,
     *   0.213, 0.715, 0.072, 0, 0,
     *   0.213, 0.715, 0.072, 0, 0,
     *   0,     0,     0, 1, 0 ]
     *   0.213 + 0.715 + 0.072 = 1
     * @return
     */
    public ColorMatrix getGreyscaleColorMatrix(){
        ColorMatrix colorMatrix = new ColorMatrix();
        //设置饱和度 0-100% 由灰色到饱和态
        colorMatrix.setSaturation(0);
        return colorMatrix;
    }

    /**
     * 得到一种灰黄色的处理效果，setScale(1, 1, 0.8f, 1) 对应的矩阵为：
     * [ 1, 0,   0, 0, 0,
     *   0, 1,   0, 0, 0,
     *   0, 0, 0.8, 0, 0,
     *   0, 0,   0, 1, 0 ]
     * @return
     */
    public ColorMatrix getSepiaColorMatrix(){

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrix colorScale = new ColorMatrix();
        //设置亮度  即颜色的相对明暗程度
        colorScale.setScale(1, 1, 0.8f, 1);

        // Convert to grayscale, then apply brown color  实际上就是一种复合处理，也即先做灰度处理再做亮度处理
        // postConcat就是矩阵的后乘运算
        colorMatrix.postConcat(colorScale);

        return colorMatrix;
    }

    /**
     * 二进制灰度处理  实际上所有的像素点都是由二进制0和1组成的  分别表示黑和白
     * @return
     */
    public ColorMatrix getBineryColorMatrix(){

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        float m = 255f;
        float t = -255 * 128f;

        ColorMatrix threshold = new ColorMatrix(new float[]{

                m, 0, 0, 1, t,
                0, m , 0, 1, t,
                0, 0, m, 1, t,
                0, 0, 0, 1, 0
        });

        //后乘矩阵
        colorMatrix.postConcat(threshold);
        return colorMatrix;
    }
}
