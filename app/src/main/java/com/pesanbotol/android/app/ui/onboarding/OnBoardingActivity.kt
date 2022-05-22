package com.pesanbotol.android.app.ui.onboarding

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.databinding.ActivityOnBoardingBinding
import com.pesanbotol.android.app.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnBoardingActivity : AppCompatActivity() {
    private var _binding: ActivityOnBoardingBinding? = null
    private val authViewModel by viewModel<AuthViewModel>()
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        binding.buttonLanjut.setOnClickListener { loginPage() }
    }

    private fun loginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        authViewModel.savePassedOnboarding()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}