package com.erkuai.weatherview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2019/9/2.
 */

public class SunnyView extends View {

    //View宽
    private float mWidth = dp2px(300f);

    //View高
    private float mHeight = dp2px(300f);

    //View中心X
    private float centerX = mWidth / 2f;

    //View中心Y
    private float centerY = mHeight / 2f;

    //外圆半径
    private float outCircleRadius = dp2px(90f);

    //内圆半径
    private float innerCircleRadius = dp2px(80f);

    //阴影半径
    private float shadowRadius = dp2px(30f);

    //外圆
    private Paint outCirclePaint;

    //阴影
    private Paint shadowPaint;

    //内圆
    private Paint innerCirclePaint;

    //黄色圆
    private Paint opacityPaint;
    private float opacityX = centerX + dp2px(60);
    private float opacityY = centerY - dp2px(60);
    private float opacityRadius = dp2px(30);

    //光晕
    private Paint sunshinePaint;
    private float sunshineX = opacityX;
    private float sunshineY = opacityY;
    private float sunshineRadius = dp2px(50);

    private ObjectAnimator sunshineAnimator = ObjectAnimator.ofFloat(this, "sunshineY", opacityY,
            opacityY - dp2px(30));

    public SunnyView(Context context) {
        this(context, null, 0);
    }

    public SunnyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunnyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        outCirclePaint = new Paint();
        outCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outCirclePaint.setShader(new RadialGradient(centerX, centerY, outCircleRadius,
                Color.parseColor("#e6e8db"), Color.parseColor("#c9e8de"), Shader.TileMode.CLAMP));

        shadowPaint = new Paint();
        shadowPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(Color.parseColor("#f98c24"));
        shadowPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.SOLID));

        innerCirclePaint = new Paint();
        innerCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setShader(new LinearGradient(centerX - innerCircleRadius,
                centerY + innerCircleRadius, centerX, centerY - innerCircleRadius,
                Color.parseColor("#fc5830"), Color.parseColor("#f98c24"), Shader.TileMode.CLAMP));


        opacityPaint = new Paint();
        opacityPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        opacityPaint.setColor(Color.parseColor("#ffeb3b"));

        sunshinePaint = new Paint();
        sunshinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        sunshinePaint.setColor(Color.parseColor("#19ffffff"));

        sunshineAnimator.setRepeatMode(ValueAnimator.REVERSE);
        sunshineAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sunshineAnimator.setDuration(5000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        sunshineAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sunshineAnimator.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, outCircleRadius, shadowPaint);
        canvas.drawCircle(centerX, centerY, outCircleRadius, outCirclePaint);
        canvas.drawCircle(centerX, centerY, innerCircleRadius, innerCirclePaint);
        canvas.save();

        canvas.drawCircle(sunshineX, sunshineY, sunshineRadius, sunshinePaint);
        sunshinePaint.setColor(Color.parseColor("#55ffffff"));
        canvas.drawCircle(sunshineX + dp2px(20), sunshineY + dp2px(18), 40, sunshinePaint);

        canvas.drawCircle(opacityX, opacityY, opacityRadius + dp2px(5), outCirclePaint);
        canvas.drawCircle(opacityX, opacityY, opacityRadius, opacityPaint);

    }

    public void setSunshineY(float sunshineY) {
        this.sunshineY = sunshineY;
        sunshineX = calculateShadowX(dp2px(60), sunshineY);
        invalidate();
    }

    private float calculateShadowX(float moveRadius, float sunshineY) {
        float lengthY = opacityY - sunshineY;
        float lengthX = (float) Math.sqrt(Math.pow(moveRadius, 2.0) - Math.pow(lengthY, 2.0));
        return opacityX - lengthX;
    }

    private int getMeasureWidth(int measureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                width = size;
                break;
            case MeasureSpec.AT_MOST:
                width = (int) mWidth;
                break;
            case MeasureSpec.EXACTLY:
                width = (int) Math.max(mWidth, size);
                break;
        }
        return width;
    }

    private int getMeasureHeight(int measureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                height = size;
                break;
            case MeasureSpec.AT_MOST:
                height = (int) mHeight;
                break;
            case MeasureSpec.EXACTLY:
                height = (int) Math.max(mHeight, size);
                break;
        }
        return height;
    }

    private int dp2px(float dipValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
