package com.kimmandoo.tv

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.BackgroundManager

class SimpleBackgroundManager(
    private val context: Activity,
) {
    private var defaultBackground: Drawable? =
        AppCompatResources.getDrawable(context, DEFAULT_BACKGROUND_RES_ID)
    private var backgroundManager: BackgroundManager =
        BackgroundManager.getInstance(context)

    init {
        backgroundManager.attach(context.window)
        @Suppress("DEPRECATION")
        context.windowManager.defaultDisplay.getMetrics(DisplayMetrics())
    }

    fun updateBackground(resource: Drawable?) {
        // 항상 메인스레드가 관리하는 방식
        backgroundManager.drawable = resource
    }

    fun clearBackground() {
        backgroundManager.drawable = defaultBackground
    }


    companion object {
        private val DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background
    }
}