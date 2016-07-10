package com.dmitrybrant.android.multitouch;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class MultiTouchCanvas extends View {
    private static final int CIRCLE_RADIUS_DP = 20;

    public interface MultiTouchStatusListener {
        void onStatus(List<Point> pointerLocations, int numPoints);
    }

    @Nullable private MultiTouchStatusListener statusListener;
    @NonNull private Paint paint;
    private int totalTouches;
    private int circleRadius;

    private List<Point> pointerLocations = new ArrayList<>();
    private int[] pointerColors = new int[] { 0xFFFFFFFF, 0xFFFF4040, 0xFF40FF40, 0xFF4040FF, 0xFFFF40FF, 0xFFFFFF40, 0xFF40FFFF };
    private int[] pointerColorsDark = new int[] { 0xFFA0A0A0, 0xFFA00000, 0xFF00A000, 0xFF0000A0, 0xFFA000A0, 0xFFA0A000, 0xFF00A0A0 };

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

    public void setStatusListener(@Nullable MultiTouchStatusListener listener) {
        statusListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        for (int i = 0; i < totalTouches; i++) {
            Point p = pointerLocations.get(i);
            paint.setColor(pointerColorsDark[i % pointerColorsDark.length]);
            canvas.drawLine(0, p.y, canvas.getWidth(), p.y, paint);
            canvas.drawLine(p.x, 0, p.x, canvas.getHeight(), paint);
            canvas.drawCircle(p.x, p.y, circleRadius * 5 / 4, paint);
            paint.setColor(pointerColors[i % pointerColors.length]);
            canvas.drawCircle(p.x, p.y, circleRadius, paint);
        }
        if (statusListener != null) {
            statusListener.onStatus(pointerLocations, totalTouches);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int action = event.getActionMasked();

        int numTouches = event.getPointerCount();
        if (numTouches > totalTouches) {
            totalTouches = numTouches;
        }
        for (int i = 0; i < numTouches; i++) {
            pointerLocations.get(i).x = (int) event.getX(i);
            pointerLocations.get(i).y = (int) event.getY(i);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                totalTouches = numTouches;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                //move indices down, and put the last one up
                if (pointerIndex < numTouches - 1) {
                    Point p = pointerLocations.get(pointerIndex);
                    pointerLocations.get(numTouches - 1).x = p.x;
                    pointerLocations.get(numTouches - 1).y = p.y;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }

        postInvalidate();
        return true;
    }

    private void init() {
        circleRadius = (int)(CIRCLE_RADIUS_DP * getResources().getDisplayMetrics().density);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        final int maxPointers = 100;
        for (int i = 0; i < maxPointers; i++) {
            pointerLocations.add(new Point());
        }
    }
}
