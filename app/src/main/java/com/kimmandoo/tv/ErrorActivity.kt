package com.kimmandoo.tv

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ErrorActivity : FragmentActivity() {
    private val errorFragment: ErrorFragment by lazy { ErrorFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testError()

    }

    @SuppressLint("CommitTransaction")
    private fun testError() {
        @Suppress("DEPRECATION")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, errorFragment).commit()
    }

}