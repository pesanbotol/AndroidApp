package com.pesanbotol.android.app.ui.detail_users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pesanbotol.android.app.databinding.ActivityDetailUsersBinding

class DetailUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}