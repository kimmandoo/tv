package com.kimmandoo.tv

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(
        vh: ViewHolder?,
        item: Any?
    ) {
        item?.let {
            val movie = item as Movie
            movie.let {
                vh?.title?.text = it.title
                vh?.subtitle?.text = it.studio
                vh?.body?.text = it.title
            }
        }
    }
}