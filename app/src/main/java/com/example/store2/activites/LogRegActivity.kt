package com.example.store2.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.store2.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogRegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)
    }
}
