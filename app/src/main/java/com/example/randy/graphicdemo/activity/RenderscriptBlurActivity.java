package com.example.randy.graphicdemo.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.example.randy.graphicdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy on 2015/11/22.
 * 这里我们使用 Renderscript来实现毛玻璃效果，也就是图片的模糊蒙层效果
 * 具体原理在印象笔记中会有说明
 */
@TargetApi(17)
public class RenderscriptBlurActivity extends SpinnerImageActivity {

    //这里选用5种模糊半径来分别看一下模糊效果 注：采用Renderscript来实现模糊效果半径最大为25


    @Override
    protected List<Option> getOptions(Bitmap original) {
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.original), original));

        for(float radius: new float[]{ 1, 4, 9, 16, 25}){
            options.add(createOption(original, radius));
        }
        return options;
    }

    public Option createOption(Bitmap orignal, float radius){

        String title = getString(R.string.blur_with_radius, radius);
        Bitmap bitmap = blur(orignal, radius);
        return new Option(title, bitmap);
    }

    //模糊化处理
    private Bitmap blur(Bitmap original, float radius){

        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blur.setInput(allocIn);
        blur.setRadius(radius);
        blur.forEach(allocOut);

        allocOut.copyTo(bitmap);

        rs.destroy();

        return bitmap;
    }

}
