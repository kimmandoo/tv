package com.kimmandoo.tv

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.DetailsOverviewRowPresenter
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.SparseArrayObjectAdapter
import com.bumptech.glide.Glide
import com.kimmandoo.tv.part7to9.PlaybackOverlayActivity
import java.io.IOException

private const val TAG = "VideoDetailsFragment"
const val MOVIE = "MOVIE"

class VideoDetailsFragment : DetailsSupportFragment() {

    // version #1
    private val fwdorPresenter by lazy {
        FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
    }

    // version #2
    private val dorPresenter by lazy {
        @Suppress("DEPRECATION")
        DetailsOverviewRowPresenter(DetailsDescriptionPresenter())
    }

    private val glideBackgroundManager by lazy {
        GlideBackgroundManager(requireActivity())
    }

    private lateinit var selectedMovie: Movie
    private lateinit var detailsRowBuilderTask: DetailsRowBuilderTask


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        selectedMovie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // intent로 값 받아오기
            activity.intent.getSerializableExtra(MOVIE, Movie::class.java) as Movie
        } else {
            activity.intent.getSerializableExtra(MOVIE) as Movie
        }
        detailsRowBuilderTask =
            DetailsRowBuilderTask(activity, selectedMovie, fwdorPresenter)
//        detailsRowBuilderTask =
//            DetailsRowBuilderTask(requireActivity(), selectedMovie, dorPresenter)
        detailsRowBuilderTask.execute()
        selectedMovie.cardImageUrl?.let {
            glideBackgroundManager.updateBackgroundWithDelay(it)
        }
    }

    override fun onStop() {
        super.onStop()
        detailsRowBuilderTask.cancel(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    inner class DetailsRowBuilderTask(
        private val activity: FragmentActivity,
        private val selectedMovie: Movie,
        private val presenter: Presenter
    ) : AsyncTask<Movie, Int, DetailsOverviewRow>() {
        override fun doInBackground(vararg params: Movie): DetailsOverviewRow {
            val row = DetailsOverviewRow(selectedMovie)
            try {
                val pixelWidth =
                    Utils.convertDpToPixel(activity.applicationContext, DETAIL_THUMB_WIDTH)
                val pixelHeight =
                    Utils.convertDpToPixel(activity.applicationContext, DETAIL_THUMB_HEIGHT)

                val poster = Glide.with(activity)
                    .asBitmap()
                    .load(selectedMovie.imageUri) // cardImageUrl 대신 imageUri 사용
//                    .load(selectedMovie.cardImageUrl)
                    .override(pixelWidth, pixelHeight)
                    .centerCrop()
                    .submit()
                    .get()

                row.setImageBitmap(activity, poster)
            } catch (e: IOException) {
                Log.w(TAG, "Failed to load image: ${e.message}")
            }
            return row
        }

        @Deprecated("Deprecated in Java")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(row: DetailsOverviewRow) {
            val sparseArrayObjectAdapter = SparseArrayObjectAdapter()
            sparseArrayObjectAdapter.set(0, Action(ACTION_PLAY_VIDEO, "Play Video"))

            sparseArrayObjectAdapter.apply {
                repeat(10) { i ->
                    set(i + 1, Action(i.toLong(), "label1", "label2"))
                }
            }
            row.actionsAdapter = sparseArrayObjectAdapter
            fwdorPresenter.onActionClickedListener = object : OnActionClickedListener {
                override fun onActionClicked(action: Action?) {
                    action?.let {
                        if (action.id == ACTION_PLAY_VIDEO) {
                            Log.d(TAG, "onActionClicked: ${action.id}")
                            val intent = Intent(
                                requireContext(),
                                PlaybackOverlayActivity::class.java
                            ).apply {
                                putExtra(MOVIE, selectedMovie)
                                putExtra("shouldStart", true)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }


            // 2nd row: ListRow
            val listRowAdapter = ArrayObjectAdapter(CardPresenter()).apply {
                repeat(10) { i ->
                    val movie = Movie(
                        id = i.toLong(),
                        title = "title$i",
                        studio = "studio$i",
                        imageUri = when {
                            i % 3 == 0 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQszAySd5NzSxVsfOR6ZllimOYiz0KrOEhgCw&s"
                            i % 3 == 1 -> "https://t4.ftcdn.net/jpg/05/78/60/99/360_F_578609973_kGyUqmJ2Gl9wGqag78ciyDbUFaB2hdU5.jpg"
                            else -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjStS_8E0EjFPkUmCuOHBnr8o2C2jiX-D8_Q&s"
                        }
                    )
                    add(movie)
                }
            }

            val headerItem = HeaderItem(0, "Related Videos")

            // Presenter 설정
            val classPresenterSelector = ClassPresenterSelector().apply {
                when (presenter) {
                    is FullWidthDetailsOverviewRowPresenter -> {
                        addClassPresenter(DetailsOverviewRow::class.java, presenter.apply {
                            initialState = FullWidthDetailsOverviewRowPresenter.STATE_SMALL
                            Log.e(TAG, "mFwdorPresenter.getInitialState: $initialState")
                        })
                    }

                    is DetailsOverviewRowPresenter -> {
                        addClassPresenter(DetailsOverviewRow::class.java, presenter.apply {
                            Log.d(TAG, "onPostExecute: ${this}")
                        })
                    }
                }
                addClassPresenter(ListRow::class.java, ListRowPresenter())
            }

            // 어댑터 설정
            val adapter = ArrayObjectAdapter(classPresenterSelector).apply {
                add(row)
                add(ListRow(headerItem, listRowAdapter))
                // 3rd row는 주석 처리된 상태로 유지
                // add(ListRow(headerItem, listRowAdapter))
            }
            this@VideoDetailsFragment.adapter = adapter
        }
    }

    companion object {
        const val DETAIL_THUMB_WIDTH = 274
        const val DETAIL_THUMB_HEIGHT = 274
        const val ACTION_PLAY_VIDEO = 1L
    }
}