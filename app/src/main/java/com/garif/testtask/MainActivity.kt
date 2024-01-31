package com.garif.testtask

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.garif.testtask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isHideMenu = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with(binding) {
            setListenerToDestroy(svSettings)
            setListenerToDestroy(svHome)
            setListenerToDestroy(svSearch)
            setListenerToDestroy(svTime)
            setListenerToDestroy(svWindow)

            btnMenu.setOnClickListener {
                selectAnimationDirection(svSettings)
                selectAnimationDirection(svHome)
                selectAnimationDirection(svSearch)
                selectAnimationDirection(svTime)
                selectAnimationDirection(svWindow)

                isHideMenu = !isHideMenu
            }
        }
    }

    private fun selectAnimationDirection(view: View) {
        if (isHideMenu)
            animateMenuButton(view, resources.getDimension(R.dimen.x0))
        else
            animateMenuButton(view, resources.getDimension(R.dimen.x35))
    }

    private fun animateMenuButton(view: View, y: Float) {
        ObjectAnimator.ofFloat(view, "translationY", dipToPixels(y)).apply {
            interpolator = AccelerateInterpolator()
            duration = 500
            start()
        }
    }


    private fun setListenerToDestroy(swipeView: SwipeView) {
        swipeView.setOnSwipeListener {
            with(binding) {
                layout.removeView(swipeView)
            }
        }
    }
}