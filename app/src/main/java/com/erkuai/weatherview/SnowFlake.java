package com.erkuai.weatherview;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Administrator on 2019/9/2.
 */

public class SnowFlake {
    private float radius;
    private float speed;
    private float angle;
    private float moveScopeX;
    private float moveScopeY;


    public SnowFlake(float radius, float speed, float angle, float moveScopeX, float moveScopeY) {
        this.radius = radius;
        this.speed = speed;
        this.angle = angle;
        this.moveScopeX = moveScopeX;
        this.moveScopeY = moveScopeY;
    }

    private Random random = new Random();
    private float presentX = random.nextInt((int) moveScopeX) + 1;
    private float presentY = random.nextInt((int) moveScopeY) + 1;
    private float presentSpeed = getSpeed();
    private float presentAngle = getAngle();
    private float presentRadius = getRadius();

    public void draw(Canvas canvas, Paint paint) {
        moveX();
        moveY();
        if (moveScopeX != 0 && moveScopeY != 0) {
            if (presentX > moveScopeX || presentY > moveScopeY || presentX < 0 || presentY < 0) {
                reset();
            }
        }
        canvas.drawCircle(presentX, presentY, presentRadius, paint);
    }


    private float getSpeed() {
        speed = random.nextFloat() + 1;
        return speed;
    }

    private float getRadius() {
        radius = random.nextInt(15);
        return radius;
    }

    private float getAngle() {
        if (angle > 30) {
            return 30;
        }
        if (angle < 0) {
            return 0;
        }
        return angle;
    }

    private void reset() {
        presentSpeed = getSpeed();
        presentAngle = getAngle();
        presentRadius = getRadius();
        presentX = random.nextInt((int) moveScopeX);
        presentY = 0;
    }

    private void moveX() {
        presentX += getSpeedX();
    }

    private void moveY() {
        presentY += getSpeedY();
    }

    private float getSpeedX() {
        return (float) (presentSpeed * Math.sin(presentAngle));
    }

    private float getSpeedY() {
        return (float) (presentSpeed * Math.cos(presentAngle));
    }

}
