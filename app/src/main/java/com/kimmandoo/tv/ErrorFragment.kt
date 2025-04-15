package com.kimmandoo.tv

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.app.ErrorSupportFragment

class ErrorFragment : ErrorSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)
        setErrorContent()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setErrorContent() {
        imageDrawable = requireContext().getDrawable(androidx.leanback.R.drawable.lb_ic_sad_cloud)
        message = requireContext().getString(R.string.error_fragment_message)
        setDefaultBackground(TRANSLUCENT)

        buttonText = requireContext().getString(R.string.dismiss_error)
        buttonClickListener = View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this@ErrorFragment)
                .commit()
            requireActivity().finish()
        }
    }

    companion object {
        private const val TRANSLUCENT = true
    }
}