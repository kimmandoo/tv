package com.kimmandoo.tv.part7

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.ControlButtonPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackControlsRowPresenter
import com.kimmandoo.tv.CardPresenter
import com.kimmandoo.tv.DetailsDescriptionPresenter
import com.kimmandoo.tv.MOVIE
import com.kimmandoo.tv.Movie

class PlaybackOverlayFragment : PlaybackSupportFragment() {

    private lateinit var selectedMovie: Movie
    private lateinit var rowAdapter: ArrayObjectAdapter
    private lateinit var playbackControlsRow: PlaybackControlsRow
    private lateinit var primaryActionsAdapter: ArrayObjectAdapter
    private lateinit var secondaryActionsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMovie = requireActivity().intent.getSerializableExtra(MOVIE) as Movie
        backgroundType = BG_LIGHT
        isFadingEnabled = false
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

        adapter = rowAdapter
    }

    private fun addPlaybackControlsRow() {
        val ctx = requireActivity()
        playbackControlsRow = PlaybackControlsRow(selectedMovie)
        rowAdapter.add(playbackControlsRow)
        val cbpSelector = ControlButtonPresenterSelector()
        primaryActionsAdapter = ArrayObjectAdapter(cbpSelector)
        secondaryActionsAdapter = ArrayObjectAdapter(cbpSelector)
        primaryActionsAdapter.apply {
            add(PlaybackControlsRow.SkipPreviousAction(ctx))
            add(PlaybackControlsRow.RewindAction(ctx))
            add(PlaybackControlsRow.PlayPauseAction(ctx))
            add(PlaybackControlsRow.FastForwardAction(ctx))
            add(PlaybackControlsRow.SkipNextAction(ctx))
        }
        secondaryActionsAdapter.apply {
            add(PlaybackControlsRow.ThumbsUpAction(ctx))
            add(PlaybackControlsRow.ThumbsDownAction(ctx))
            add(PlaybackControlsRow.RepeatAction(ctx))
            add(PlaybackControlsRow.ShuffleAction(ctx))
            add(PlaybackControlsRow.HighQualityAction(ctx))
            add(PlaybackControlsRow.ClosedCaptioningAction(ctx))
            add(PlaybackControlsRow.MoreActions(ctx))
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

    companion object {
        private const val TAG = "PlaybackOverlayFragment"
    }
}