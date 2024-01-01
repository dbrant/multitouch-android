package com.dmitrybrant.android.multitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MultiTouchCanvas extends View {

    public MultiTouchCanvas(Context context) {
        super(context);
        init();
    }

    public MultiTouchCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiTouchCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public interface MultiTouchStatusListener {
        void onStatus(List<Point> pointerLocations, int numPoints);
    }

    public MultiTouchStatusListener statusListener = null;
    private final Paint paint = new Paint();
    private int totalTouches = 0;
    private int circleRadius = 0;
    private final List<Point> pointerLocations = new ArrayList<>();
    private final int[] pointerColors = new int[] { -0x1, -0xbfc0, -0xbf00c0, -0xbfbf01, -0xbf01, -0xc0, -0xbf0001 };
    private final int[] pointerColorsDark = new int[] { -0x5f5f60, -0x600000, -0xff6000, -0xffff60, -0x5fff60, -0x5f6000, -0xff5f60 };
    private final int CIRCLE_RADIUS_DP = 20;

    private void init() {
        circleRadius = (int)(CIRCLE_RADIUS_DP * getResources().getDisplayMetrics().density);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3f);
        int maxPointers = 100;
        for (int i = 0; i < maxPointers; i++) {
            pointerLocations.add(new Point());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        for (int i = 0; i < totalTouches; i++) {
            Point p = pointerLocations.get(i);
            paint.setColor(pointerColorsDark[i % pointerColorsDark.length]);
            canvas.drawLine(0f, p.y, getWidth(), p.y, paint);
            canvas.drawLine(p.x, 0f, p.x, getHeight(), paint);
            canvas.drawCircle(p.x, p.y, circleRadius * 5f / 4f, paint);
            paint.setColor(pointerColors[i % pointerColors.length]);
            canvas.drawCircle(p.x, p.y, circleRadius, paint);
        }
        if (statusListener != null) {
            statusListener.onStatus(pointerLocations, totalTouches);
        }
    }

    public void renderTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int numTouches = event.getPointerCount();
        if (numTouches > totalTouches) {
            totalTouches = numTouches;
        }
        for (int i = 0; i < numTouches; i++) {
            pointerLocations.get(i).x = (int)event.getX(i);
            pointerLocations.get(i).y = (int)event.getY(i);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_CANCEL:
                totalTouches = numTouches;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //move indices down, and put the last one up
                if (pointerIndex < numTouches - 1) {
                    Point p = pointerLocations.get(pointerIndex);
                    pointerLocations.get(numTouches - 1).x = p.x;
                    pointerLocations.get(numTouches - 1).y = p.y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        postInvalidate();
    }
}
