package com.example.randy.graphicdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.randy.graphicdemo.R;

/**
 * Created by admin on 2016/1/4.
 * 自定义圆形view
 * 这里采用的是BitmapShader来处理：需要设置的属性有bitmap画笔、边的画笔、填充的画笔
 */
public class CircleImageView extends ImageView {

    //image缩放的类型
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    //bitmap
    private Bitmap bitmap;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    //默认值
    private static final int DEFAULT_BORDER_WIDTH = 3;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    //设置圆形view的边宽和颜色
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private boolean mBorderOverlay = DEFAULT_BORDER_OVERLAY;
    //填充颜色
    private int mFillColor = DEFAULT_FILL_COLOR;

    //bitmap shader
    private BitmapShader bitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();
    //shader matrix
    private final Matrix mShaderMatrix = new Matrix();
    //3个画笔
    private Paint mBitmapPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private Paint mFillPaint = new Paint();

    //一些状态标志位
    private boolean mSetupPending;
    private boolean mReady;

    public CircleImageView(Context context) {
//        super(context);
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH);
        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY);
        mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color, DEFAULT_FILL_COLOR);

        a.recycle();

        init();
    }


    private void init(){

        super.setScaleType(SCALE_TYPE);
        mReady = true;//已准备好
        if(mSetupPending){
            setUp();
            mSetupPending = false;
        }
    }

    private void setUp(){

        if(!mReady){
            mSetupPending = true;
            return;
        }

        if(0 == getWidth() && 0 == getHeight()){
            return;
        }

        if(null == bitmap){
            invalidate();
            return;
        }
        //bitmap着色器，采用bitmap对象来进行着色
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(bitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);//设置画笔的空心线宽

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(mFillColor);
        mFillPaint.setAntiAlias(true);

        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        bitmapShader.setLocalMatrix(mShaderMatrix);
    }

//    private void initView(){
//
//
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }


    //重写设置bitmap的方法


    @Override
    public void setImageDrawable(Drawable drawable) {
        //调用超类的该方法，将drawable对象设置到imageview中
        super.setImageDrawable(drawable);
        //根据drawable对象获取bitmap
        bitmap = getBitmapFromDrawable(drawable);
        setUp();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        bitmap = getBitmapFromDrawable(getDrawable());
        setUp();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        bitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
        setUp();
    }


    /**
     * 根据Drawable对象获取bitmap对象
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable){

        if(null == drawable){
            return null;
        }

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
