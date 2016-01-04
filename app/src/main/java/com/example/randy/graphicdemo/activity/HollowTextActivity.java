package com.example.randy.graphicdemo.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.randy.graphicdemo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by randy on 2015/11/22.
 * 这个demo用于测试PathEffect效果：
 * Path类会按照指定的路径绘制图形，如果将画笔的Style设定为Stroke，则会看到由一条条线组成图形
 * 而采用PathEffect可以在路径的绘制中实现一些效果：该类继承于Object，通常使用其子类
 * CornerPathEffect：将Path的各个连接线段之间的夹角用一种更平滑的方式连接，类似于圆弧与切线的效果
 * 一般的，通过CornerPathEffect(float radius)指定一个具体的圆弧半径来实例化一个CornerPathEffect
 * DashPathEffect：将Path的线段虚线化
 * 构造函数为DashPathEffect(float[] intervals, float offset)，其中intervals为虚线的ON和OFF数组，该数组的length必须大于等于2，phase为绘制时的偏移量
 * DiscretePathEffect：打散Path的线段，使得在原来路径的基础上发生打散效果
 * 一般的，通过构造DiscretePathEffect(float segmentLength,float deviation)来构造一个实例，其中，segmentLength指定最大的段长，deviation指定偏离量
 * PathDashPathEffect：使用Path图形来填充当前的路径，其构造函数为PathDashPathEffect (Path shape, float advance, float phase,PathDashPathEffect.Stylestyle)
 * shape则是指填充图形，advance指每个图形间的间距，phase为绘制时的偏移量，style为该类自由的枚举值，有三种情况：Style.ROTATE、Style.MORPH和Style.TRANSLATE
 * 其中ROTATE的情况下，线段连接处的图形转换以旋转到与下一段移动方向相一致的角度进行转转，MORPH时图形会以发生拉伸或压缩等变形的情况与下一段相连接，TRANSLATE时，图形会以位置平移的方式与下一段相连接
 * ComposePathEffect：组合效果，这个类需要两个PathEffect参数来构造一个实例，ComposePathEffect (PathEffect outerpe,PathEffect innerpe)
 * 表现时，会首先将innerpe表现出来，然后再在innerpe的基础上去增加outerpe的效果
 * SumPathEffect：叠加效果，这个类也需要两个PathEffect作为参数SumPathEffect(PathEffect first,PathEffect second)
 * 但与ComposePathEffect不同的是，在表现时，会分别对两个参数的效果各自独立进行表现，然后将两个效果简单的重叠在一起显示出来
 * 关于参数phase
 * 在存在phase参数的两个类里，如果phase参数的值不停发生改变，那么所绘制的图形也会随着偏移量而不断的发生变动，这个时候，看起来这条线就像动起来了一样
 * Paint的一些效果研究：  http://wpf814533631.iteye.com/blog/1847661
 */
public class HollowTextActivity extends Activity {

    private TextView textView;
    private Spinner spinner;

    private Path star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hollow_text);

        textView = (TextView)findViewById(R.id.text);
        int strokeWidth = getResources().getDimensionPixelSize(R.dimen.dashed_text_stroke_width);
        final HollowSpan span = new HollowSpan(strokeWidth);

        final String text = textView.getText().toString();
        Log.e("TAG", "text---->" + text);  //A
        final SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(span, 0, text.length(), 0);
        star(10);
        spinner = (Spinner)findViewById(R.id.spinner);
        final List<Option> options = Arrays.asList(
                new Option(getString(R.string.solid), null),
                new Option(getString(R.string.dash), getDashPathEffect(strokeWidth)),
                new Option(getString(R.string.round_dash), getCornerPathEffect(strokeWidth)),
                new Option(getString(R.string.triangle), getTrianglePathEffect(strokeWidth)),
                new Option(getString(R.string.discrete), getDiscretePathEffect(strokeWidth)),
                new Option(getString(R.string.sum), getSumPathEffect(strokeWidth))
        );

        ArrayAdapter<Option> adapter = new ArrayAdapter<Option>(
                this, android.R.layout.simple_spinner_item, options
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Option option = options.get(position);
                Log.e("TAG", "position--->" + position);
                span.setPathEffect(option.effect);
                textView.setText(spannableString, TextView.BufferType.SPANNABLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 虚线化处理
     * @param strokeWidth
     * @return
     */
    private PathEffect getDashPathEffect(int strokeWidth){

        return new DashPathEffect(new float[]{ strokeWidth *3, strokeWidth}, 0);
    }

    /**
     * 先以光滑的圆弧连接，然后虚线化
     * @param strokeWidth
     * @return
     */
    private PathEffect getCornerPathEffect(int strokeWidth){
        PathEffect dash = getDashPathEffect(strokeWidth);
        PathEffect corner = new CornerPathEffect(strokeWidth);
        return new ComposePathEffect(dash, corner);
    }

    /**
     *
     * @param strokeWidth
     * @return
     */
    private PathEffect getTrianglePathEffect(int strokeWidth){

        return new PathDashPathEffect(
                getTriangle(strokeWidth),
                strokeWidth,
                0.0f,
                PathDashPathEffect.Style.ROTATE
        );
    }

    private Path getTriangle(float size){
        Path path = new Path();
        float half = size / 2;
        path.moveTo(-half, -half);
        path.lineTo(half, -half);
        path.lineTo(0, half);
        path.close();
        return path;
    }

    /**
     * 绘制打散的效果  A字符显示的效果是一种扭曲的形状
     * @param strokeWidth
     * @return
     */
    public PathEffect getDiscretePathEffect(int strokeWidth){

        return new DiscretePathEffect( strokeWidth * 3, strokeWidth / 2);
    }

    public PathEffect getSumPathEffect(int strokeWidth){
        PathEffect cpe1 = new PathDashPathEffect(star, strokeWidth, 0.0f, PathDashPathEffect.Style.MORPH);
        PathEffect cpe2 = new DiscretePathEffect(strokeWidth / 2, strokeWidth * 3);
        return new SumPathEffect(cpe1, cpe2);
    }

    private void star(float length){
        star = new Path();
        float dis1 = (float)((length/2)/Math.tan((54f/180)*Math.PI));
        float dis2 = (float)(length*Math.sin((72f/180)*Math.PI));
        float dis3 = (float)(length*Math.cos((72f/180)*Math.PI));
        star.moveTo(length/2, 0);
        star.lineTo(length/2-dis3, dis2);
        star.lineTo(length, dis1);
        star.lineTo(0, dis1);
        star.lineTo(length/2+dis3, dis2);
        star.lineTo(length/2, 0);
    }

    //关于Span的博文  http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0305/2535.html
    private static class HollowSpan extends ReplacementSpan {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private int width;

        public HollowSpan(int strokeWidth) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
        }

        public void setPathEffect(PathEffect effect) {
            paint.setPathEffect(effect);
        }

        @Override
        public int getSize(
                Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            this.paint.setColor(paint.getColor());

            width = (int) (paint.measureText(text, start, end) + this.paint.getStrokeWidth());
            return width;
        }

        @Override
        public void draw(
                Canvas canvas, CharSequence text, int start, int end,
                float x, int top, int y, int bottom, Paint paint) {
            path.reset();
            paint.getTextPath(text.toString(), start, end, x, y, path);
            path.close();

            canvas.translate(this.paint.getStrokeWidth() / 2, 0);
            canvas.drawPath(path, this.paint);
            canvas.translate(-this.paint.getStrokeWidth() / 2, 0);
        }
    }

    /*private static class HollowSpan extends ReplacementSpan{
        //创建画笔，抗锯齿
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建Path类实例
        private final Path path = new Path();
        private int width;

        public HollowSpan(int strokeWidth) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
        }

        public void setPathEffect(PathEffect effect){
            paint.setPathEffect(effect);
        }


        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            this.paint.setColor(paint.getColor());
            width = (int)(paint.measureText(text, start, end) + this.paint.getStrokeWidth());
            return width;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

            //这里出现笔误，不是调用paint  而是调用path.reset()
            paint.reset();
            paint.getTextPath(text.toString(), start, end, x, y, path);
            path.close();

            canvas.translate(this.paint.getStrokeWidth() / 2, 0);
            canvas.drawPath(path, this.paint);
            canvas.translate(-this.paint.getStrokeWidth()/2, 0);
        }
    }*/

    protected static class Option {
        public final String title;
        public final PathEffect effect;

        public Option(String title, PathEffect effect) {
            this.title = title;
            this.effect = effect;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
