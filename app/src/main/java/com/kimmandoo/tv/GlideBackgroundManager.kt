package com.kimmandoo.tv

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.leanback.app.BackgroundManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.net.URISyntaxException
import java.util.Timer
import java.util.TimerTask


private const val TAG = "SimpleBackgroundManager"

class GlideBackgroundManager(
    private val context: Activity,
) {
    private var defaultBackground: Drawable? =
        AppCompatResources.getDrawable(context, DEFAULT_BACKGROUND_RES_ID)
    private var backgroundManager: BackgroundManager =
        BackgroundManager.getInstance(context)
    private val metrics = DisplayMetrics()
    private var backgroundURI: Uri? = null
    private var backgroundTimer: Timer? = null
    private var backgroundTarget: GlideBackgroundManagerTarget =
        GlideBackgroundManagerTarget(backgroundManager)

    // handler에 대한 관찰 필요
    private val handler = Handler(Looper.getMainLooper())

    init {
        backgroundManager.attach(context.window)
        @Suppress("DEPRECATION")
        context.windowManager.defaultDisplay.getMetrics(metrics)
    }

    private fun startTimer() {
        backgroundTimer?.cancel()

        backgroundTimer = Timer()
        backgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY)
    }

    private inner class UpdateBackgroundTask() : TimerTask() {
        override fun run() {
            handler.post {
                backgroundURI?.let {
                    updateBackground(it)
                }
            }
        }
    }

//    fun updateBackground(resource: Drawable?) {
//        // 항상 메인스레드가 관리하는 방식
//        backgroundManager.drawable = resource
//    }

    fun updateBackground(uri: Uri) {
        // Glide를 사용해 thread-safe하게 이미지 로드
        try {
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .override(metrics.widthPixels, metrics.heightPixels)
                .centerCrop()
                .error(defaultBackground)
                .into(backgroundTarget)
        } catch (e: Exception) {
            Log.e(TAG, "updateBackground failed: $e")
            backgroundManager.drawable = defaultBackground
        }
    }

    fun updateBackgroundWithDelay(uri: String) {
        try {
            updateBackgroundWithDelay(uri.toUri())
        } catch (e: URISyntaxException) {
            Log.d(TAG, "updateBackgroundWithDelay: $e")
        }
    }

    fun updateBackgroundWithDelay(uri: Uri) {
        backgroundURI = uri
        startTimer()
    }

    fun clearBackground() {
        backgroundManager.drawable = defaultBackground
    }

    class GlideBackgroundManagerTarget(
        private val backgroundManager: BackgroundManager
    ) : CustomTarget<Bitmap>() {

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            backgroundManager.setBitmap(resource)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            errorDrawable?.let {
                backgroundManager.setDrawable(it)
            }
        }

        override fun onLoadStarted(placeholder: Drawable?) {
            // Do nothing, default_background manager has its own transitions
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            // Optional: Handle cleanup if needed
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as GlideBackgroundManagerTarget
            return backgroundManager == that.backgroundManager
        }

        override fun hashCode(): Int {
            return backgroundManager.hashCode()
        }
    }


    companion object {
        private val DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background
        private val BACKGROUND_UPDATE_DELAY = 500L
    }
}