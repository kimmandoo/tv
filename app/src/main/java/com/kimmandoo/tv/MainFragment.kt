package com.kimmandoo.tv

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.app.BrowseSupportFragment

@RequiresApi(Build.VERSION_CODES.M)
class MainFragment: BrowseSupportFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUIElements()
    }

    private fun setUpUIElements(){
        title = "Hello TV"
        badgeDrawable = ResourcesCompat.getDrawable(resources, R.drawable.banner, null)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.fastlane_background, null)
        searchAffordanceColor = resources.getColor(R.color.search_opaque, null)
    }
}