package com.example.randy.graphicdemo.activity;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.randy.graphicdemo.R;

/**
 * Created by randy on 2015/11/22.
 * BlurMaskFilter   指定了一个模糊的样式和半径来处理Paint的边缘
 */
public class BlurMaskFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur_mask_filter);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        for (BlurMaskFilter.Blur style : BlurMaskFilter.Blur.values()) {
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.TextAppearance_Huge_Green);
            applyFilter(textView, style);

            LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            container.addView(textView, params);
        }
    }

    private void applyFilter(
            TextView textView, BlurMaskFilter.Blur style) {
        if (Build.VERSION.SDK_INT >= 11) {
            //ViewUtil.setSoftwareLayerType(textView);
            //关闭硬件加速器
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        textView.setText(style.name());
        //前面一个控制阴影的宽度，后面一个参数控制阴影效果
        BlurMaskFilter filter = new BlurMaskFilter(textView.getTextSize() / 10, style);
        textView.getPaint().setMaskFilter(filter);
    }
}
