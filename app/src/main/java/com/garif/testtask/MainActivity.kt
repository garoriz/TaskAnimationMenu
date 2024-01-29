package com.garif.testtask

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.garif.testtask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with (binding) {
            btnMenu.setOnClickListener {
                animateMenu(ibSettings)
                animateMenu(ibHome)
                animateMenu(ibSearch)
                animateMenu(ibTime)
                animateMenu(ibWindow)
            }
        }
    }

    private fun animateMenu(view: View) {
        if (!view.isVisible) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            view.visibility = View.VISIBLE
            view.startAnimation(animation)
        } else {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
            view.visibility = View.GONE
            view.startAnimation(animation)
        }
    }
}