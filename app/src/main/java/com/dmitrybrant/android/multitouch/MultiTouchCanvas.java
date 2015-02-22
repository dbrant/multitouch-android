package com.dmitrybrant.android.multitouch;

import java.util.ArrayList;
import java.util.List;

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
    private MultiTouchActivity parentActivity;

    private Paint paint;
    private int totalTouches = 0;
    private int circleRadius;

    private List<Point> pointerLocations = new ArrayList<>();
    private int[] pointerColors = new int[] { 0xFFFFFFFF, 0xFFFF4040, 0xFF40FF40, 0xFF4040FF, 0xFFFF40FF, 0xFFFFFF40, 0xFF40FFFF };
    private int[] pointerColorsDark = new int[] { 0xFFa0a0a0, 0xFFa00000, 0xFF00a000, 0xFF0000a0, 0xFFa000a0, 0xFFa0a000, 0xFF00a0a0 };

    public MultiTouchCanvas(Context context) {
        super(context);
        init(context);
    }

    public MultiTouchCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiTouchCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        circleRadius = (int)(CIRCLE_RADIUS_DP * getResources().getDisplayMetrics().density);
        parentActivity = (MultiTouchActivity) context;
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
        String str = "Touches detected: " + Integer.toString(totalTouches);
        for (int i = 0; i < totalTouches; i++) {
            str += "\n";
            str += pointerLocations.get(i).x + ", " + pointerLocations.get(i).y;
        }
        parentActivity.updateInfo(str);
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
}
