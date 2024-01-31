package com.garif.testtask

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs


class SwipeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    fun setOnSwipeListener(listener: () -> Unit) {
        swipeCallback = listener
    }

    fun destroy() {
        dragHelper.abort()
        swipeCallback = null
    }

    companion object {
        private const val AUTO_SWIPE_VELOCITY = 800.0
    }

    private var previousState = ViewDragHelper.STATE_IDLE
    private var draggingLeft = false
    private var draggingRight = false
    private var draggedThreshold = false
    private var swipeComplete = false
    private var swipeCallback: (() -> Unit)? = null

    private val dragHelperCallback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int) = child === getSlidingView()
        override fun getViewHorizontalDragRange(child: View) = width
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int) =
            left.coerceAtMost(width)

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int,
        ) {
            draggingLeft = abs(left) > width / 20 && dx <= 0
            draggingRight = abs(left) > width / 20 && dx >= 0
            draggedThreshold = abs(left) < width / 2
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (xvel < -AUTO_SWIPE_VELOCITY) {
                swipeComplete = true
                dragHelper.flingCapturedView(-width, 0, -width, 0)
                ViewCompat.postInvalidateOnAnimation(this@SwipeView)
            } else if (xvel > AUTO_SWIPE_VELOCITY) {
                swipeComplete = true
                dragHelper.flingCapturedView(width, 0, width, 0)
                ViewCompat.postInvalidateOnAnimation(this@SwipeView)
            } else {
                var finalLeft = width - dipToPixels(56f)
                if (draggedThreshold) {
                    finalLeft = -dipToPixels(40f)
                    swipeComplete = true
                }
                if (dragHelper.settleCapturedViewAt(finalLeft.toInt(), 0)) {
                    ViewCompat.postInvalidateOnAnimation(this@SwipeView)
                }
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state != previousState && state == ViewDragHelper.STATE_IDLE && swipeComplete) {
                getSlidingView()?.post(swipeCallback)
            }
            previousState = state
        }
    }

    private val dragHelper: ViewDragHelper = ViewDragHelper.create(this, 1f, dragHelperCallback)

    private fun getSlidingView(): View? {
        return getChildAt(0)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            dragHelper.processTouchEvent(ev)
            parent?.let {
                when (ev.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        parent.requestDisallowInterceptTouchEvent(draggingLeft)
                        parent.requestDisallowInterceptTouchEvent(draggingRight)
                    }

                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        draggingLeft = false
                        draggingRight = false
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return event?.takeIf { dragHelper.shouldInterceptTouchEvent(event) } != null || super.onInterceptTouchEvent(
            event
        )
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }
}