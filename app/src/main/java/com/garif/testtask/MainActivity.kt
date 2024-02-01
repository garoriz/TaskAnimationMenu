package com.garif.testtask

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
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
                selectAnimationDirection(svSettings, resources.getDimension(R.dimen.x35))
                selectAnimationDirection(svHome, resources.getDimension(R.dimen.x28))
                selectAnimationDirection(svSearch, resources.getDimension(R.dimen.x21))
                selectAnimationDirection(svTime, resources.getDimension(R.dimen.x14))
                selectAnimationDirection(svWindow, resources.getDimension(R.dimen.x7))

                isHideMenu = !isHideMenu
            }
        }
    }

    private fun selectAnimationDirection(view: View, y: Float) {
        if (isHideMenu)
            animateMenuButton(view, resources.getDimension(R.dimen.x0))
        else
            animateMenuButton(view, y)
    }

    private fun animateMenuButton(view: View, y: Float) {
        ObjectAnimator.ofFloat(view, "translationY", dipToPixels(y)).apply {
            duration = 800
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