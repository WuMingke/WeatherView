package com.erkuai.weatherview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/9/2.
 */

public class SnowyView extends View {

    //View宽
    private float mWidth = dp2px(300f);

    //View高
    private float mHeight = dp2px(300f);

    //View中心X
    private float centerX = mWidth / 2f;

    //View中心Y
    private float centerY = mHeight / 2f;

    private float outCircleRadius = dp2px(90f);

    private float innerCircleRadius = dp2px(80f);

    private float shadowRadius = dp2px(30f);

    //雪人
    private float snowyManHeaderRadius = dp2px(12);
    private float snowyManBodyRadius = dp2px(25);
    private float snowyManHeaderX = centerX;
    private float snowyManHeaderY = centerY + outCircleRadius - snowyManHeaderRadius - snowyManBodyRadius * 2;
    private float snowyManBodyX = centerX - dp2px(3);
    private float snowyManBodyY = centerY + outCircleRadius - snowyManBodyRadius - dp2px(5);
    private Paint snowyManPaint;

    private RectF snowyManHandRect;
    private Paint snowyManHandPaint;

    private Paint outPaint;

    private Paint innerCirclePaint;

    private Paint snowyPaint;

    private List<SnowFlake> snowFlakes = new ArrayList<>();

    public SnowyView(Context context) {
        this(context, null, 0);
    }

    public SnowyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        snowyManPaint = new Paint();
        snowyManPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        snowyManPaint.setColor(Color.parseColor("#e6e8db"));

        snowyManHandRect = new RectF(centerX - dp2px(40f),
                centerY, centerX + dp2px(40f), snowyManBodyY - dp2px(8f));
        snowyManHandPaint = new Paint();
        snowyManHandPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        snowyManHandPaint.setColor(Color.BLACK);
        snowyManHandPaint.setStyle(Paint.Style.STROKE);
        snowyManHandPaint.setStrokeWidth(5);
        snowyManHandPaint.setAlpha(120);

        outPaint = new Paint();
        outPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outPaint.setColor(Color.parseColor("#e6e8db"));
        outPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.SOLID));

        innerCirclePaint = new Paint();
        innerCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setShader(new LinearGradient(centerX - innerCircleRadius, centerY + innerCircleRadius,
                centerX, centerY - innerCircleRadius,
                Color.parseColor("#e0e2e5"), Color.parseColor("#758595"), Shader.TileMode.CLAMP));

        snowyPaint = new Paint();
        snowyPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        snowyPaint.setColor(Color.parseColor("#e6e8db"));
        snowyPaint.setAlpha(100);

        for (int i = 0; i < 30; i++) {
            SnowFlake snowFlake = new SnowFlake(0, 0, 0, mWidth, mHeight);
            snowFlakes.add(snowFlake);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, outCircleRadius, outPaint);
        canvas.drawCircle(centerX, centerY, innerCircleRadius, innerCirclePaint);
        canvas.drawArc(snowyManHandRect, 155f, -120f, false, snowyManHandPaint);
        canvas.drawCircle(snowyManHeaderX, snowyManHeaderY, snowyManHeaderRadius, snowyManPaint);
        canvas.drawCircle(snowyManBodyX, snowyManBodyY, snowyManBodyRadius, snowyManPaint);

        for (int i = 0; i < snowFlakes.size(); i++) {
            snowFlakes.get(i).draw(canvas, snowyPaint);
        }
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 5);
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
