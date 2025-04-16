package com.kimmandoo.tv.part7to9

import android.media.session.PlaybackState
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.ControlButtonPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction
import androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction.INDEX_PLAY
import androidx.leanback.widget.PlaybackControlsRowPresenter
import com.kimmandoo.tv.CardPresenter
import com.kimmandoo.tv.DetailsDescriptionPresenter
import com.kimmandoo.tv.MOVIE
import com.kimmandoo.tv.Movie
import com.kimmandoo.tv.Utils
import kotlinx.coroutines.Runnable


class PlaybackOverlayFragment : PlaybackSupportFragment() {

    private lateinit var selectedMovie: Movie
    private lateinit var rowAdapter: ArrayObjectAdapter
    private lateinit var playbackControlsRow: PlaybackControlsRow
    private lateinit var primaryActionsAdapter: ArrayObjectAdapter
    private lateinit var secondaryActionsAdapter: ArrayObjectAdapter
    private lateinit var skipPreviousAction: Action
    private lateinit var rewindAction: Action
    private lateinit var playPauseAction: Action
    private lateinit var fastForwardAction: Action
    private lateinit var skipNextAction: Action
    private lateinit var thumbsUpAction: Action
    private lateinit var thumbsDownAction: Action
    private lateinit var repeatAction: Action
    private lateinit var shuffleAction: Action
    private lateinit var highQualityAction: Action
    private lateinit var closedCaptioningAction: Action
    private lateinit var moreActions: Action
    private var currentPlaybackState: Int = 0
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var currentItem: Int = 0
    private val items = mutableListOf<Movie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMovie = requireActivity().intent.getSerializableExtra(MOVIE) as Movie
        backgroundType = BG_LIGHT
        handler = Handler(Looper.getMainLooper())
        isFadingEnabled = false

//        items = MovieProvider.getMovieItem()
        currentItem = selectedMovie.id.toInt() - 1
        setUpRows()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
    }

    private fun setUpRows() {
        val ps = ClassPresenterSelector()
        val playbackControlsRowPresenter =
            PlaybackControlsRowPresenter(DetailsDescriptionPresenter())
        ps.apply {
            addClassPresenter(PlaybackControlsRow::class.java, playbackControlsRowPresenter)
            addClassPresenter(ListRow::class.java, ListRowPresenter())
        }
        rowAdapter = ArrayObjectAdapter(ps)
        addPlaybackControlsRow()
        addOtherRows()

        playbackControlsRowPresenter.onActionClickedListener = object : OnActionClickedListener {
            override fun onActionClicked(action: Action?) {
                when (action?.id) {
                    playPauseAction.id -> {
                        Log.d(TAG, "onActionClicked: playPauseAction")
                        Log.d(
                            TAG,
                            "onActionClicked: ${playPauseAction.label1}, ${playPauseAction.label2}"
                        )
                        @Suppress("DEPRECATION")
                        togglePlayback(action.id.toInt() == PlayPauseAction.PLAY)
                    }

                    skipNextAction.id -> {
                        Log.d(TAG, "onActionClicked: skipNextAction")
                        next(currentPlaybackState == PlaybackState.STATE_PLAYING)
                    }

                    skipPreviousAction.id -> {
                        Log.d(TAG, "onActionClicked: skipPreviousAction")
                        prev(currentPlaybackState == PlaybackState.STATE_PLAYING)
                    }

                    fastForwardAction.id -> {
                        Log.d(TAG, "onActionClicked: fastForwardAction")
                        fastForward()
                    }

                    rewindAction.id -> {
                        Log.d(TAG, "onActionClicked: rewindAction")
                        rewind()
                    }
                }
                if (action is PlaybackControlsRow.MultiAction) {
                    Log.d(TAG, "onActionClicked: ${action}")
                    notifyChanged(action)
                    if (action is PlaybackControlsRow.ThumbsUpAction ||
                        action is PlaybackControlsRow.ThumbsDownAction ||
                        action is PlaybackControlsRow.RepeatAction ||
                        action is PlaybackControlsRow.ShuffleAction ||
                        action is PlaybackControlsRow.HighQualityAction ||
                        action is PlaybackControlsRow.ClosedCaptioningAction
                    ) {
                        (action as PlaybackControlsRow.MultiAction).nextIndex()
                    }
                }
            }
        }

        adapter = rowAdapter
    }

    private fun addPlaybackControlsRow() {
        val ctx = requireActivity()
        playbackControlsRow = PlaybackControlsRow(selectedMovie)
        rowAdapter.add(playbackControlsRow)
        val cbpSelector = ControlButtonPresenterSelector()
        primaryActionsAdapter = ArrayObjectAdapter(cbpSelector)
        secondaryActionsAdapter = ArrayObjectAdapter(cbpSelector)

        skipNextAction = PlaybackControlsRow.SkipPreviousAction(ctx)
        rewindAction = PlaybackControlsRow.RewindAction(ctx)
        playPauseAction = PlaybackControlsRow.PlayPauseAction(ctx)
        fastForwardAction = PlaybackControlsRow.FastForwardAction(ctx)
        skipPreviousAction = PlaybackControlsRow.SkipNextAction(ctx)

        primaryActionsAdapter.apply {
            add(skipPreviousAction)
            add(rewindAction)
            add(playPauseAction)
            add(fastForwardAction)
            add(skipNextAction)
        }

        thumbsUpAction = PlaybackControlsRow.ThumbsUpAction(ctx)
        thumbsDownAction = PlaybackControlsRow.ThumbsDownAction(ctx)
        repeatAction = PlaybackControlsRow.RepeatAction(ctx)
        shuffleAction = PlaybackControlsRow.ShuffleAction(ctx)
        highQualityAction = PlaybackControlsRow.HighQualityAction(ctx)
        closedCaptioningAction = PlaybackControlsRow.ClosedCaptioningAction(ctx)
        moreActions = PlaybackControlsRow.MoreActions(ctx)

        secondaryActionsAdapter.apply {
            add(thumbsUpAction)
            add(thumbsDownAction)
            add(repeatAction)
            add(shuffleAction)
            add(highQualityAction)
            add(closedCaptioningAction)
            add(moreActions)
        }
        playbackControlsRow.apply {
            primaryActionsAdapter = this@PlaybackOverlayFragment.primaryActionsAdapter
            secondaryActionsAdapter = this@PlaybackOverlayFragment.secondaryActionsAdapter
        }
    }

    private fun addOtherRows() {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        val movie = Movie(
            id = 0,
            title = "무비무비",
            studio = "무비ddddddd무비",
            imageUri = "https://png.pngitem.com/pimgs/s/49-497492_kawaii-cake-pixel-art-hd-png-download.png",
            description = "무비무비",
            cardImageUrl = "https://png.pngitem.com/pimgs/s/49-497492_kawaii-cake-pixel-art-hd-png-download.png"
        )
        listRowAdapter.add(movie)
        listRowAdapter.add(movie)
        val header = HeaderItem(0, "OtherRows")
        rowAdapter.add(ListRow(header, listRowAdapter))
    }

    private fun notifyChanged(action: Action) {
        var adapter = primaryActionsAdapter
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1)
            return
        }
        adapter = secondaryActionsAdapter
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1)
            return
        }
    }

    private fun togglePlayback(playPause: Boolean) {
        Log.d(TAG, "togglePlayback: clicked $playPause")
        (requireActivity() as PlaybackOverlayActivity).playPause(playPause)
        playbackStateChanged()
    }

    private fun playbackStateChanged() {
        if (currentPlaybackState != PlaybackState.STATE_PLAYING) {
            currentPlaybackState = PlaybackState.STATE_PLAYING
            startProgressAutomation()
            isFadingEnabled = true
            playPauseAction.icon = resources.getDrawable(android.R.drawable.ic_media_pause)
            (playPauseAction as PlayPauseAction).index = PlayPauseAction.INDEX_PAUSE
            notifyChanged(playPauseAction)
        } else if (currentPlaybackState != PlaybackState.STATE_PAUSED) {
            currentPlaybackState = PlaybackState.STATE_PAUSED
            stopProgressAutomation()
            isFadingEnabled = false
            playPauseAction.icon = resources.getDrawable(android.R.drawable.ic_media_play)
            (playPauseAction as PlayPauseAction).index = INDEX_PLAY
            notifyChanged(playPauseAction)
        }
        val currentTime = (requireActivity() as PlaybackOverlayActivity).pos
        playbackControlsRow.currentPosition = currentTime.toLong()
        playbackControlsRow.bufferedPosition = currentTime.toLong() + SIMULATED_BUFFERED_TIME
    }

    private fun getUpdatePeriod(): Int {
        if (view == null || playbackControlsRow.totalTime <= 0) {
            return DEFAULT_UPDATE_PERIOD
        }
        return UPDATE_PERIOD.coerceAtLeast(playbackControlsRow.getTotalTime() / requireView().width)
    }

    private fun startProgressAutomation() {
        if (runnable == null) {
            runnable = object : Runnable {
                override fun run() {
                    val updatePeriod = getUpdatePeriod()
                    val currentTime = playbackControlsRow.currentTime + updatePeriod
                    val totalTime = playbackControlsRow.totalTime
                    playbackControlsRow.currentTime = currentTime
                    playbackControlsRow.bufferedProgress = currentTime + SIMULATED_BUFFERED_TIME
                    if (totalTime > 0 && totalTime <= currentTime) {
                        stopProgressAutomation()
                        next(true)
                    } else {
                        handler?.postDelayed(this, updatePeriod.toLong())
                    }
                }
            }
            handler?.postDelayed(runnable!!, getUpdatePeriod().toLong())
        }
    }

    private fun stopProgressAutomation() {
        if (handler != null && runnable != null) {
            handler?.removeCallbacks(runnable!!)
            runnable = null
        }
    }

    private fun fastForward() {
        (requireActivity() as PlaybackOverlayActivity).fastForward()
        val currentTime = (requireActivity() as PlaybackOverlayActivity).getPosition()
        playbackControlsRow.currentPosition = currentTime.toLong()
        playbackControlsRow.bufferedPosition = (currentTime + SIMULATED_BUFFERED_TIME).toLong()
    }

    private fun rewind() {
        (requireActivity() as PlaybackOverlayActivity).rewind()
        val currentTime = (requireActivity() as PlaybackOverlayActivity).getPosition()
        playbackControlsRow.currentPosition = currentTime.toLong()
        playbackControlsRow.bufferedPosition = (currentTime + SIMULATED_BUFFERED_TIME).toLong()
    }

    private fun next(autoPlay: Boolean) {
        if (++currentItem >= items.size) {
            currentItem = 0 // 리스트 끝에서 처음으로 이동시키기
        }
        if (autoPlay) {
            currentPlaybackState = PlaybackState.STATE_PAUSED
        }
        val movie = items[currentItem]
        movie?.let {
            with(requireActivity() as PlaybackOverlayActivity) {
                setVideoPath(it.videoUrl!!)
                setPlaybackState(PlaybackOverlayActivity.LeanbackPlayingState.PAUSED)
                playPause(autoPlay)
            }
        }
        playbackStateChanged()
        updatePlaybackRow(currentItem)
    }

    private fun prev(autoPlay: Boolean) {
        if (--currentItem < 0) {
            currentItem = items.size - 1 // 리스트 끝에서 처음으로 이동시키기
        }
        if (autoPlay) {
            currentPlaybackState = PlaybackState.STATE_PAUSED
        }
        val movie = items[currentItem]
        movie?.let {
            with(requireActivity() as PlaybackOverlayActivity) {
                setVideoPath(it.videoUrl!!)
                setPlaybackState(PlaybackOverlayActivity.LeanbackPlayingState.PAUSED)
                playPause(autoPlay)
            }
        }
        playbackStateChanged()
        updatePlaybackRow(currentItem)
    }

    private fun updatePlaybackRow(currentItem: Int) {
        if (playbackControlsRow.item != null) {
            val item = playbackControlsRow.item as Movie
            val curItem = item.copy(
                title = items[currentItem].title,
                studio = items[currentItem].title,
            )
            rowAdapter.notifyArrayItemRangeChanged(0, 1)
            val duration = Utils.getDuration(items[currentItem].videoUrl)
            Log.d(TAG, "updatePlaybackRow: $duration + ${items[currentItem].videoUrl}")
            playbackControlsRow.totalTime = duration.toInt()
            playbackControlsRow.currentTime = 0
            playbackControlsRow.bufferedProgress = 0
        }
        if (SHOW_IMAGE) {
            // card업데이트
        }
    }


    companion object {
        private const val TAG = "PlaybackOverlayFragment"
        private const val SIMULATED_BUFFERED_TIME: Int = 10000
        private const val DEFAULT_UPDATE_PERIOD: Int = 1000
        private const val UPDATE_PERIOD: Int = 16
        private const val CARD_WIDTH: Int = 200
        private const val CARD_HEIGHT: Int = 240
        private const val SHOW_IMAGE: Boolean = true
    }
}