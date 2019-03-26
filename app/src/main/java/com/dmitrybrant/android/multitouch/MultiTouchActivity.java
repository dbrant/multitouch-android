package com.dmitrybrant.android.multitouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MultiTouchActivity extends Activity implements MultiTouchCanvas.MultiTouchStatusListener {
    private TextView txtInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtInfo = findViewById(R.id.txtInfo);
        ((MultiTouchCanvas) findViewById(R.id.multiTouchView)).setStatusListener(this);

        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.getBackground().setAlpha(128);
        btnAbout.setOnClickListener(v -> showAboutDialog());
    }

    @Override
    public void onStatus(List<Point> pointerLocations, int numPoints) {
        String str = String.format(getResources().getString(R.string.num_touches), Integer.toString(numPoints));
        for (int i = 0; i < numPoints; i++) {
            str += "\n";
            str += pointerLocations.get(i).x + ", " + pointerLocations.get(i).y;
        }
        txtInfo.setText(str);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, null)
                .setTitle(R.string.about)
                .setMessage(R.string.str_about)
                .create()
                .show();
    }
}


