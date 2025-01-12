package com.dmitrybrant.android.multitouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dmitrybrant.android.multitouch.MultiTouchCanvas.MultiTouchStatusListener;

import java.util.List;

public class MultiTouchActivity extends Activity implements MultiTouchStatusListener {
    private TextView txtInfo;
    private MultiTouchCanvas multiTouchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtInfo = findViewById(R.id.txtInfo);
        multiTouchView = findViewById(R.id.multiTouchView);
        multiTouchView.statusListener = this;

        findViewById(R.id.btnAbout).setOnClickListener(v -> showAboutDialog());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.containerView).setOnApplyWindowInsetsListener((v, insets) -> {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) findViewById(R.id.btnAbout).getLayoutParams();
                params.topMargin = insets.getSystemWindowInsetTop();
                params.bottomMargin = insets.getSystemWindowInsetBottom();
                params.leftMargin = insets.getSystemWindowInsetLeft();
                params.rightMargin = insets.getSystemWindowInsetRight();
                params = (FrameLayout.LayoutParams) findViewById(R.id.txtInfo).getLayoutParams();
                params.topMargin = insets.getSystemWindowInsetTop();
                params.bottomMargin = insets.getSystemWindowInsetBottom();
                params.leftMargin = insets.getSystemWindowInsetLeft();
                params.rightMargin = insets.getSystemWindowInsetRight();
                return insets.consumeSystemWindowInsets();
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event != null) {
            multiTouchView.renderTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public void onStatus(List<Point> pointerLocations, int numPoints) {
        StringBuilder str = new StringBuilder(String.format(getString(R.string.num_touches), numPoints));
        for (int i = 0; i < numPoints; i++) {
            str.append("\n");
            str.append(pointerLocations.get(i).x);
            str.append(", ");
            str.append(pointerLocations.get(i).y);
        }
        txtInfo.setText(str);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle(R.string.about)
                .setMessage(R.string.str_about)
                .create()
                .show();
    }
}
