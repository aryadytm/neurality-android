package com.wemadefun.neurality.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wemadefun.neurality.firebaseutils.FireCrashlytics

fun ImageView.setImageWithGlide(resId: Int) {
    try {
        Glide.with(this.context).load(resId).into(this)
    } catch (e: Exception) {
        FireCrashlytics.report(e)
    }
}

fun ImageView.setImageWithGlide(resDrawable: Drawable?) {
    try {
        Glide.with(this.context).load(resDrawable).into(this)
    } catch (e: Exception) {
        FireCrashlytics.report(e)
    }
}