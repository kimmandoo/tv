package com.kimmandoo.tv

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.leanback.widget.Presenter
import com.kimmandoo.tv.databinding.ItemGridBinding

class GridItemPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder? {
        val inflater = LayoutInflater.from(parent?.context)
        val view = ItemGridBinding.inflate(inflater, parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder?,
        item: Any?
    ) {
        val binding = ItemGridBinding.bind(viewHolder?.view!!)
        binding.tvItem.text = item as String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }
}