package com.kimmandoo.tv

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter

@RequiresApi(Build.VERSION_CODES.O)
class CardPresenter : Presenter() {

    inner class CustomViewHolder(view: ImageCardView) : Presenter.ViewHolder(view) {
        var movie: Movie? = null
        val cardView: ImageCardView = view
        val defaultCardImage: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder? {
        val context = parent!!.context
        val cardView = ImageCardView(context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setBackgroundColor(context.getColor(R.color.fastlane_background))
        }
        return CustomViewHolder(cardView)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder?,
        item: Any?
    ) {
        val movie = item as Movie
        val holder = viewHolder as ViewHolder
        with((holder as CustomViewHolder).cardView) {
            titleText = movie.title
            contentText = movie.studio
            setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
            mainImage = holder.defaultCardImage
        }
        holder.movie = movie
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }

    companion object {
        const val CARD_WIDTH: Int = 313
        const val CARD_HEIGHT: Int = 176
    }
}