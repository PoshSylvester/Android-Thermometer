package com.xcz1899.thermometer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 文件说明:自定义温度计
 * 温度计分为三层，每层包含一个底部circle和上部rect。
 * 第一层：温度计的外部边框
 * 第二层：温度计的背景
 * 第三层：温度计的真实刻度值
 * 温度计的左边还包含一个刻度尺。
 * Created by xcz on 16/11/17.
 */

public class Thermometer extends View {
    //温度计外部边框中的 circle半径和rect半径（即宽度的一半）
    private float outerCircleRadius;
    private float outerRectRadius;
    private Paint outerPaint;
    //温度计背景中的 circle半径和rect半径（即宽度的一半）
    private float middleCircleRadius;
    private float middleRectRadius;
    private Paint middlePaint;
    //温度计真实刻度值中的 circle半径和rect半径（即宽度的一半）
    private float innerCircleRadius;
    private float innerRectRadius;
    private Paint innerPaint;

    private Paint degreePaint;

    private static final float DEGREE_WIDTH = 20;//温度计左边刻度的宽度（x轴的宽度）
    private static final float MAX_TEMP = 50;//最大温度值
    private static final float MIN_TEMP = -30;//最小温度值
    private static final float RANGE_TEMP = 80;//温度区间 50-(-30)=80
    private float currentTemp = 20;//当前温度

    /**
     * 设置温度
     *
     * @param currentTemp 当前温度
     */
    public void setCurrentTemp(float currentTemp) {
        if (currentTemp > MAX_TEMP) {
            this.currentTemp = MAX_TEMP;
        } else if (currentTemp < MIN_TEMP) {
            this.currentTemp = MIN_TEMP;
        } else {
            this.currentTemp = currentTemp;
        }
        invalidate();
    }

    public Thermometer(Context context) {
        super(context);
        init(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Thermometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * 初始化
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Thermometer);
        outerCircleRadius = typedArray.getDimension(R.styleable.Thermometer_radius, 20f);
        int outerColor = typedArray.getColor(R.styleable.Thermometer_outerColor, Color.GRAY);
        int middleColor = typedArray.getColor(R.styleable.Thermometer_middleColor, Color.WHITE);
        int innerColor = typedArray.getColor(R.styleable.Thermometer_innerColor, Color.RED);
        typedArray.recycle();


        outerRectRadius = outerCircleRadius / 2;//温度计 上部的宽度为下部的一半
        outerPaint = new Paint();
        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.FILL);

        middleCircleRadius = outerCircleRadius - 5;//middle和outer之间的间隙
        middleRectRadius = outerRectRadius - 5;
        middlePaint = new Paint();
        middlePaint.setColor(middleColor);
        middlePaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = middleCircleRadius - 10;//middle和inner之间的间隙
        innerRectRadius = middleRectRadius - 10;
        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);


        degreePaint = new Paint();
        degreePaint.setStrokeWidth(2);//温度计刻度尺的宽度
        degreePaint.setColor(outerColor);
        degreePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //View的长宽
        float height = getHeight();
        float width = getWidth();

        //底部圆形的圆心位置
        float CircleCenterX = width / 2;
        float CircleCenterY = height - outerCircleRadius;

        float outerStartY = 0;  //outer的起始Y坐标
        float middleStartY = outerStartY + 5;//middle的起始Y坐标

        float innerEffectStartY = middleStartY + middleRectRadius + 10;//有效的inner Rect的起始Y
        float innerEffectEndY = CircleCenterY - outerCircleRadius - 10;//有效的inner Rect的结束Y
        float innerRectHeight = innerEffectEndY - innerEffectStartY;//inner Rect的有效长度，即温度的有效范围
        float innerStartY = innerEffectStartY + (currentTemp - MIN_TEMP) / RANGE_TEMP * innerRectHeight;//inner的起始Y坐标

        //画最外层的圆头矩形
        canvas.drawRoundRect(CircleCenterX - outerRectRadius, outerStartY, CircleCenterX + outerRectRadius, CircleCenterY, outerRectRadius, outerRectRadius, outerPaint);
        //画最外层的圆
        canvas.drawCircle(CircleCenterX, CircleCenterY, outerCircleRadius, outerPaint);

        //画中间层的圆头矩形
        canvas.drawRoundRect(CircleCenterX - middleRectRadius, middleStartY, CircleCenterX + middleRectRadius, CircleCenterY, middleRectRadius, middleRectRadius, middlePaint);
        //画中间层的圆
        canvas.drawCircle(CircleCenterX, CircleCenterY, middleCircleRadius, middlePaint);

        //画内部的圆头矩形
        canvas.drawRect(CircleCenterX - innerRectRadius, innerStartY, CircleCenterX + innerRectRadius, CircleCenterY, innerPaint);
        //画内部的圆
        canvas.drawCircle(CircleCenterX, CircleCenterY, innerCircleRadius, innerPaint);

        //画刻度
        float tmp = innerEffectStartY;//innerEffectStartY 刻度的起始位置
        while (tmp <= innerEffectEndY) {//innerEffectEndY 刻度的终止位置
            canvas.drawLine(CircleCenterX - outerRectRadius - DEGREE_WIDTH, tmp, CircleCenterX - outerRectRadius, tmp, degreePaint);
            tmp += (innerEffectEndY - innerEffectStartY) / 8;
        }

    }
}
