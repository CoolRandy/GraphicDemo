package com.example.randy.graphicdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.randy.graphicdemo.R;

/**
 * Created by randy on 2015/11/21.
 */
public class SpotlightView extends View{


    private int targetId;

    private Bitmap mMask;
    private float mMaskX;
    private float mMaskY;
    private float mMaskScale;
    private Matrix mShaderMatrix = new Matrix();

    private Bitmap mTargetBitmap;
    private final Paint mPaint = new Paint();

    //动画回调接口
    private AnimationSetupCallback mCallback;

    public interface AnimationSetupCallback{
        void onSetupAnimation(SpotlightView spotlightView);
    }

    public void setAnimationSetupCallback(AnimationSetupCallback callback){
        mCallback = callback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float maskW = mMask.getWidth() / 2.0f;
        float maskH = mMask.getHeight() / 2.0f;

        float x = mMaskX - maskW * mMaskScale;
        float y = mMaskY - maskH * mMaskScale;

        mShaderMatrix.setScale(1.0f / mMaskScale, 1.0f / mMaskScale);
        mShaderMatrix.preTranslate(-x, -y);

        mPaint.getShader().setLocalMatrix(mShaderMatrix);

        canvas.translate(x, y);
        canvas.scale(mMaskScale, mMaskScale);
        canvas.drawBitmap(mMask, 0.0f, 0.0f, mPaint);
    }

    /**
     * 该方法在onDraw方法之前调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                createShader();
                setmMaskScale(1.0f);

                if(mCallback != null){
                    mCallback.onSetupAnimation(SpotlightView.this);
                }

                //getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public float getmMaskX() {
        return mMaskX;
    }

    public void setmMaskX(float mMaskX) {
        this.mMaskX = mMaskX;
        invalidate();
    }

    public float getmMaskY() {
        return mMaskY;
    }

    public void setmMaskY(float mMaskY) {
        this.mMaskY = mMaskY;
        invalidate();
    }

    public float getmMaskScale() {
        return mMaskScale;
    }

    public void setmMaskScale(float mMaskScale) {
        this.mMaskScale = mMaskScale;
        invalidate();
    }

    public SpotlightView(Context context) {
        super(context);
    }

    public SpotlightView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpotlightView, 0, 0);
        try{
            targetId = a.getResourceId(R.styleable.SpotlightView_target, 0);
            int maskId = a.getResourceId(R.styleable.SpotlightView_mask, 0);
            Log.e("TAG", "targetId: " + targetId);
            mMask = convertToAlphaMask(BitmapFactory.decodeResource(getResources(), maskId));
        }catch (Exception e){
            //e.printStackTrace();
            Log.e("TAG", "Error while creating the view:" + e);
        }finally {
            a.recycle();
        }
    }

   /* public SpotlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpotlightView, 0, 0);
        try{
            targetId = a.getResourceId(R.styleable.SpotlightView_target, 0);
            int maskId = a.getResourceId(R.styleable.SpotlightView_mask, 0);
            Log.e("TAG", "targetId: " + targetId);
            mMask = convertToAlphaMask(BitmapFactory.decodeResource(getResources(), maskId));
        }catch (Exception e){
            //e.printStackTrace();
            Log.e("TAG", "Error while creating the view:" + e);
        }finally {
            a.recycle();
        }
    }*/


    /**
     * Shader  用于渲染图像以及一些几何图形
     * 使用步骤：1、构建Shader对象  2、通过Paint的setShader()方法渲染对象  3、将该Painter对象绘制到屏幕上即可
     * 有不同的子类，分别用于渲染不同的效果
     * BitmapShader用于图像渲染；ComposeShader用于混合渲染；LinearGradient用于线性渲染；RadialGradient用于环形渲染；SweepGradient则用于梯度渲染
     * 这里采用BitmapShader
     */
    /**
     * 这里Shader.TileMode有3中参数可选：CLAMP、REPEAT和MIRROR
     * CLAMP的作用是如果渲染器超出原始边界范围，则会复制边缘颜色对超出范围的区域进行着色
     * REPEAT的作用是在横向和纵向上以平铺的形式重复渲染位图
     * MIRROR的作用是在横向和纵向上以镜像的方式重复渲染位图
     * @param bm
     * @return
     */
    private static Shader createShader(Bitmap bm){

        return new BitmapShader(bm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    private void createShader(){
        View target = getRootView().findViewById(targetId);
        Log.e("TAG", "target--->" + target);//null
        mTargetBitmap = createBitmap(target);
        Shader targetShader = createShader(mTargetBitmap);
        mPaint.setShader(targetShader);
    }

    private static Bitmap createBitmap(View target){

        Bitmap b = Bitmap.createBitmap(target.getWidth(), target.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        target.draw(c);
        return b;
    }

    /**
     *
     * @param bm
     * @return
     */
    private static Bitmap convertToAlphaMask(Bitmap bm){

        //根据原图的宽高以及指定格式创建新的位图  由于不能直接对原位图进行操作
        Bitmap b = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ALPHA_8);
        //创建一个画布，采用指定的位图进行绘制，这个位图必须是可变的  mutable
        Canvas canvas = new Canvas(b);
        //绘制指定的位图  从(0, 0)开始，没有画笔
        canvas.drawBitmap(bm, 0.0f, 0.0f, null);
        return b;
    }


    public float computeMaskScale(float d) {
        // Let's assume the mask is square
        return d / (float) mMask.getHeight();
    }

}
