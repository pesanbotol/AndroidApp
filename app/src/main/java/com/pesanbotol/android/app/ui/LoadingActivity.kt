package com.pesanbotol.android.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.ui.landing.LandingActivity
import com.pesanbotol.android.app.ui.login.LoginActivity
import com.pesanbotol.android.app.ui.onboarding.OnBoardingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoadingActivity : AppCompatActivity() {
    private val authViewModel by viewModel<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        supportActionBar?.hide()
        if (authViewModel.isLoggedIn()) {
            println("Logged In")
            val intent = Intent(this, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            println("Not LoggedIn")
            authViewModel.getPassedOnboarding().observe(this) {
                if (it == true) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                } else {
                    val intent = Intent(this, OnBoardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

        }


    }
}