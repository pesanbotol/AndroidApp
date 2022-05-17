package com.pesanbotol.android.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pesanbotol.android.app.databinding.ActivitySignUpBinding
import com.pesanbotol.android.app.utility.Utils

class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvLogin.setOnClickListener { loginPage() }

        Utils.setTextColor(this, binding.iAgreeWith, 13, 18)
        Utils.setTextColor(this, binding.iAgreeWith, 23, 30)
    }

    private fun loginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}