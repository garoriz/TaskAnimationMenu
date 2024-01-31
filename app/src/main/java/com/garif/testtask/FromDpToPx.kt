package com.garif.testtask

import android.content.res.Resources
import android.util.TypedValue

fun dipToPixels(dipValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dipValue,
        Resources.getSystem().displayMetrics
    )
}