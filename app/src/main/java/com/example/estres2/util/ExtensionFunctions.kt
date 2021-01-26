package com.example.estres2.util

import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat

fun Resources.setIconDrawableAndChangeColor(iconId: Int, colorTint: Int? = null): Drawable? {
    val icon = ResourcesCompat.getDrawable(this, iconId, null)
    colorTint?.let {
        icon?.colorFilter = PorterDuffColorFilter(
                ResourcesCompat.getColor(this, it, null),
                PorterDuff.Mode.SRC_ATOP
        )
    }
    return icon
}