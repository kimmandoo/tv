package com.kimmandoo.tv.part7to9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.kimmandoo.tv.MOVIE
import com.kimmandoo.tv.Movie
import com.kimmandoo.tv.Utils
import com.kimmandoo.tv.databinding.ActivityPlaybackOverlayBinding

private const val TAG = "PlaybackOverlayActivity"

class PlaybackOverlayActivity : FragmentActivity() {
    private val binding by lazy {
        ActivityPlaybackOverlayBinding.inflate(layoutInflater)
    }

    private var playbackState = LeanbackPlayingState.IDLE
    var pos = 0
    private var startTimeMillis = 0L
    private var duration = -1L

    enum class LeanbackPlayingState {
        PLAYING, PAUSED, IDLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(binding.root)
        loadViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
        binding.videoView.suspend()
        binding.videoView.setVideoPath(null)
    }

    private fun loadViews() {
        binding.apply {
            videoView.apply {
                isFocusable = false
                isFocusableInTouchMode = false
            }
            val movie = intent.getSerializableExtra(MOVIE) as Movie
            movie.videoUrl?.let {
                setVideoPath(it)
            }
        }
    }

    fun setVideoPath(videoUrl: String) {
        setPosition(0)
        binding.videoView.setVideoPath(videoUrl)
        startTimeMillis = 0
        duration = Utils.getDuration(videoUrl)
        Log.d(TAG, "setVideoPath: $videoUrl + $duration")
    }

    private fun stopPlayback() {
        binding.videoView.stopPlayback()
    }

    private fun setPosition(position: Int) {
        if(position > duration){
            pos = duration.toInt()
        }else if(position < 0){
            pos = 0
            startTimeMillis = System.currentTimeMillis()
        }else{
            pos = position
        }
        binding.videoView.seekTo(pos)
        startTimeMillis = System.currentTimeMillis()
        Log.d(TAG, "position set to $pos")
    }

    fun getPosition(): Int {
        return pos
    }

    fun setPlaybackState(state: LeanbackPlayingState) {
        playbackState = state
    }

    fun playPause(doPlay: Boolean) {
        if (playbackState == LeanbackPlayingState.IDLE) setUpCallbacks()
        if (doPlay && playbackState != LeanbackPlayingState.PLAYING) {
            playbackState = LeanbackPlayingState.PLAYING
            if (pos > 0) {
                binding.videoView.seekTo(pos)
            }
            binding.videoView.start()
            startTimeMillis = System.currentTimeMillis()
        } else {
            playbackState = LeanbackPlayingState.PAUSED
            val timeElapsed = System.currentTimeMillis() - startTimeMillis
            pos = pos + timeElapsed.toInt()
            binding.videoView.pause()
        }
    }

    fun fastForward() {
        if (duration != -1L) {
            setPosition(binding.videoView.currentPosition + (10 * 1000))
            binding.videoView.seekTo(pos)
        }
        Log.d(
            TAG,
            "fastForward: duration=$duration, currentPosition=${binding.videoView.currentPosition}, pos=$pos"
        )
    }

    fun rewind() {
        setPosition(binding.videoView.currentPosition - (10 * 1000))
        binding.videoView.seekTo(pos)
    }

    private fun setUpCallbacks() {
        with(binding.videoView) {
            setOnErrorListener { _, _, _ ->
                stopPlayback()
                playbackState = LeanbackPlayingState.IDLE
                false
            }
            setOnPreparedListener {
                if (playbackState == LeanbackPlayingState.PLAYING) {
                    start()
                }
            }
            setOnCompletionListener {
                playbackState = LeanbackPlayingState.IDLE
            }
        }
    }
}