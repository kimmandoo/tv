package com.kimmandoo.tv.part7

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.kimmandoo.tv.R

private const val TAG = "PlaybackOverlayActivity"

class PlaybackOverlayActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(R.layout.activity_playback_overlay)
    }
}