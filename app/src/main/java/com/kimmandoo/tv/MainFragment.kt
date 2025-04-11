package com.kimmandoo.tv

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter

@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : BrowseSupportFragment() {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUIElements()
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
        rowsAdapter.add(ListRow(gridItemPresenterHeader, gridRowAdapter))


        val cardPresenterHeader = HeaderItem(1, "CardPresenter")
        val cardPresenter = CardPresenter()
        val cardRowAdapter = ArrayObjectAdapter(cardPresenter)
        repeat(10) { idx ->
            cardRowAdapter.add(Movie(idx.toLong(), "Movie $idx", "Studio $idx"))
        }
        rowsAdapter.add(ListRow(cardPresenterHeader, cardRowAdapter))

        adapter = rowsAdapter
    }
}