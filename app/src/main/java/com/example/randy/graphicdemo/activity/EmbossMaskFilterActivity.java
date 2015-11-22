package com.example.randy.graphicdemo.activity;

import android.app.Activity;
import android.graphics.EmbossMaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.randy.graphicdemo.R;

/**
 * Created by randy on 2015/11/22.
 * 设置MaskFilter，可以用不同的MaskFilter实现滤镜的效果，如滤化，立体等
 * MaskFilter类可以为Paint分配边缘效  对MaskFilter的扩展可以对一个Paint边缘的alpha通道应用转换
 * EmbossMaskFilter  指定了光源的方向和环境光强度来添加浮雕效果
 * 要应用一个MaskFilter，可以使用setMaskFilter方法，并传递给它一个MaskFilter对象
 * EmbossMaskFilter可以被应用到任意Paint对象上来实现一种“浮雕图案”的效果，无论是text文本或者shape/content(能够绘制到画布上的内容)
 * 根据定义，浮雕emboss效果设计成使得内容有着轻微的凸起感，这种效果是通过让元素的一面高亮显示，相反的一面显示暗色
 * 根据构造参数的配置可以让其显示不同的效果，下面来详细分析一下这几个参数的作用：
 * Direction: Where is the light source coming from?
 * 这里是一个数组，包含三个元素x/y/z，他们相互组合确定了光源的方向  x坐标表示水平的布置 y坐标表示垂直布置 z坐标表示how far above the screen the theoretical light source exists.
 *
 * ambient是一个0.0f-1.0f的代表光源亮度的值
 * specular控制亮度的反射强度
 * blurRadius控制边缘的清晰度，也是模糊度
 *
 */
public class EmbossMaskFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emboss_filter);

        TextView emboss = (TextView)findViewById(R.id.emboss);
        // direction 选择光源方向  ambient 设置环境光亮度  specular 选择要应用的反射等级  blurRadius 向mask设置一定级别的模糊
        applyFilter(emboss, new float[] { 0f, 1f, 0.5f }, 0.8f, 3f, 3f);

        //这里要实现的效果就是内阴影效果  inner shadow
        // Deboss parameters from http://wiresareobsolete.com/2012/04/textview-inner-shadows/
        TextView deboss = (TextView)findViewById(R.id.emboss);
        applyFilter(deboss, new float[] { 0f, -1f, 0.5f }, 0.8f, 15f, 1f);

    }

    private void applyFilter(TextView textView, float[] direction, float ambient, float specular, float blurRadius){

        if(Build.VERSION.SDK_INT >= 11) {
            //关闭硬件加速器
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
        textView.getPaint().setMaskFilter(embossMaskFilter);
    }
}
