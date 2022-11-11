package com.dmitrybrant.android.multitouch

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.dmitrybrant.android.multitouch.MultiTouchCanvas.MultiTouchStatusListener

class MultiTouchActivity : Activity(), MultiTouchStatusListener {
    private lateinit var txtInfo: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        txtInfo = findViewById(R.id.txtInfo)
        findViewById<MultiTouchCanvas>(R.id.multiTouchView).statusListener = this

        findViewById<View>(R.id.btnAbout).setOnClickListener { showAboutDialog() }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            findViewById<View>(R.id.containerView).setOnApplyWindowInsetsListener { v, insets ->
                val params = findViewById<View>(R.id.btnAbout).layoutParams as FrameLayout.LayoutParams
                params.topMargin = insets.systemWindowInsetTop
                params.bottomMargin = insets.systemWindowInsetBottom
                params.leftMargin = insets.systemWindowInsetLeft
                params.rightMargin = insets.systemWindowInsetRight
                insets.consumeSystemWindowInsets()
            }
        }
    }

    override fun onStatus(pointerLocations: List<Point>, numPoints: Int) {
        val str = StringBuilder(String.format(getString(R.string.num_touches), numPoints))
        for (i in 0 until numPoints) {
            str.append("\n")
            str.append(pointerLocations[i].x)
            str.append(", ")
            str.append(pointerLocations[i].y)
        }
        txtInfo.text = str
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle(R.string.about)
                .setMessage(R.string.str_about)
                .create()
                .show()
    }
}
