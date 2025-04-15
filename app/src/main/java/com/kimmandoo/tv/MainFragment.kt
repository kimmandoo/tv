package com.kimmandoo.tv

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.DetailsFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter


private const val TAG = "MainFragment"

@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : BrowseSupportFragment() {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    private lateinit var simpleBackgroundManager: SimpleBackgroundManager
    private lateinit var glideBackgroundManager: GlideBackgroundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        simpleBackgroundManager = SimpleBackgroundManager(requireActivity())
        glideBackgroundManager = GlideBackgroundManager(requireActivity())
        setUpUIElements()
        setUpEventListener()
        loadRows()
    }

    private fun setUpUIElements() {
//        title = "Hello TV"
        badgeDrawable = ResourcesCompat.getDrawable(resources, R.drawable.banner, null)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.fastlane_background, null)
        searchAffordanceColor = resources.getColor(R.color.search_opaque, null)
    }

    private fun loadRows() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val gridItemPresenterHeader = HeaderItem(0, "GridItemPresenter")
        val gridItemPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(gridItemPresenter)
        repeat(10) { idx ->
            gridRowAdapter.add("Item $idx")
        }
        gridRowAdapter.add("ErrorFragment")
        rowsAdapter.add(ListRow(gridItemPresenterHeader, gridRowAdapter))


        val cardPresenterHeader = HeaderItem(1, "CardPresenter")
        val cardPresenter = CardPresenter()
        val cardRowAdapter = ArrayObjectAdapter(cardPresenter)
        repeat(10) { idx ->
            when {
                idx % 3 == 0 -> {
                    cardRowAdapter.add(
                        Movie(
                            idx.toLong(),
                            "Movie $idx",
                            "Studio $idx",
                            imageUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQszAySd5NzSxVsfOR6ZllimOYiz0KrOEhgCw&s"
                        )
                    )
                }

                idx % 3 == 1 -> {
                    cardRowAdapter.add(
                        Movie(
                            idx.toLong(),
                            "Movie $idx",
                            "Studio $idx",
                            imageUri = "https://t4.ftcdn.net/jpg/05/78/60/99/360_F_578609973_kGyUqmJ2Gl9wGqag78ciyDbUFaB2hdU5.jpg"
                        )
                    )
                }

                else -> {
                    cardRowAdapter.add(
                        Movie(
                            idx.toLong(),
                            "title: $idx",
                            "Studio $idx",
                            imageUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjStS_8E0EjFPkUmCuOHBnr8o2C2jiX-D8_Q&s"
                        )
                    )
                }
            }
        }
        rowsAdapter.add(ListRow(cardPresenterHeader, cardRowAdapter))

        adapter = rowsAdapter
    }

    private fun setUpEventListener() {
        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewSelectedListener() : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            Log.d(TAG, "Selected item: $item")
            item?.let {
                // 처음에 로드 될 때도 onItemSelected가 불리는데, 이 때 null이 들어옴.
                if (item is String) {
                    glideBackgroundManager.updateBackgroundWithDelay("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTs-Zs1jAhbmypFTiTem5s6YzJpLB4tyD2F_Q&s")
                } else {
                    glideBackgroundManager.updateBackgroundWithDelay((item as Movie).imageUri.toString())
                }
            }
        }
    }

    private inner class ItemViewClickedListener() : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            // itemView 클릭하면 나옴
            item?.let {
                when (it) {
                    is Movie -> {
                        val movie = it as Movie
                        Log.d(TAG, "Item: " + item.toString())
                        val intent = Intent(requireContext(), DetailsActivity::class.java)
                        intent.putExtra(MOVIE, movie)
                        requireActivity().startActivity(intent)
                    }

                    is String -> {
                        if (item == "ErrorFragment") {
                            val intent = Intent(requireContext(), ErrorActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}