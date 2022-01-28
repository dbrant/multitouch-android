package com.dmitrybrant.android.multitouch

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Point
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.dmitrybrant.android.multitouch.MultiTouchCanvas.MultiTouchStatusListener
import com.dmitrybrant.android.multitouch.databinding.MainBinding

class MultiTouchActivity : Activity(), MultiTouchStatusListener {
    private lateinit var binding: MainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.multiTouchView.statusListener = this

        binding.btnAbout.background.alpha = 128
        binding.btnAbout.setOnClickListener { showAboutDialog() }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerView)) { _, insets ->
            val params = binding.btnAbout.layoutParams as FrameLayout.LayoutParams
            params.topMargin = insets.systemWindowInsetTop
            params.bottomMargin = insets.systemWindowInsetBottom
            params.leftMargin = insets.systemWindowInsetLeft
            params.rightMargin = insets.systemWindowInsetRight
            insets.consumeSystemWindowInsets()
        }
    }

    override fun onStatus(pointerLocations: List<Point>, numPoints: Int) {
        var str = String.format(resources.getString(R.string.num_touches), numPoints.toString())
        for (i in 0 until numPoints) {
            str += "\n"
            str += pointerLocations[i].x.toString() + ", " + pointerLocations[i].y
        }
        binding.txtInfo.text = str
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, null)
                .setTitle(R.string.about)
                .setMessage(R.string.str_about)
                .create()
                .show()
    }
}
