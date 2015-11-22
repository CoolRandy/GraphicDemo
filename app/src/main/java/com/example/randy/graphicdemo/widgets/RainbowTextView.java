package com.example.randy.graphicdemo.widgets;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.randy.graphicdemo.R;

/**
 * Created by randy on 2015/11/20.
 */
public class RainbowTextView extends TextView {

    public RainbowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RainbowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RainbowTextView(Context context) {
        super(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int[] rainbow = getRainbowColor();
        Shader shader = new LinearGradient(0, 0, 0, w, rainbow, null, Shader.TileMode.MIRROR);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        shader.setLocalMatrix(matrix);

        getPaint().setShader(shader);
    }


    private int[] getRainbowColor(){

        return new int[]{

                getResources().getColor(R.color.rainbow_red),
                getResources().getColor(R.color.rainbow_yellow),
                getResources().getColor(R.color.rainbow_green),
                getResources().getColor(R.color.rainbow_blue),
                getResources().getColor(R.color.rainbow_purple)
        };
    }


}
