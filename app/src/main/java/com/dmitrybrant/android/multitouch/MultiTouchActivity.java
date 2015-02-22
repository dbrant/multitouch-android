package com.dmitrybrant.android.multitouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MultiTouchActivity extends Activity {
    private TextView txtInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        Button btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.getBackground().setAlpha(128);
        btnAbout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                View layout = getLayoutInflater().inflate(R.layout.about, null);
                TextView txtAbout = (TextView) layout.findViewById(R.id.txtAbout);
                txtAbout.setText(MultiTouchActivity.this.getString(R.string.str_about));
                AlertDialog alertDialog = new AlertDialog.Builder(MultiTouchActivity.this).create();
                alertDialog.setTitle("About...");
                alertDialog.setView(layout);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void updateInfo(String text) {
        txtInfo.setText(text);
    }
}


