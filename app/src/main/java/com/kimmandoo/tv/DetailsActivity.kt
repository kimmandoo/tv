package com.kimmandoo.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.kimmandoo.tv.databinding.ActivityDetailsBinding

class DetailsActivity : FragmentActivity() {
    private val binding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}