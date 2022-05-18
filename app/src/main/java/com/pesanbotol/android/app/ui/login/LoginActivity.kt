package com.pesanbotol.android.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.ui.signup.SignUpActivity
import com.pesanbotol.android.app.databinding.ActivityLoginBinding
import com.pesanbotol.android.app.ui.landing.LandingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val authViewModel by viewModel<AuthViewModel>()
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvSignup.setOnClickListener { signUpPage() }
        binding.btnLogin.setOnClickListener {
            authViewModel.handleSignIn(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
        authViewModel.authState.observe(this) {
            when (it) {
                is StateHandler.Loading -> {
                    showLoading()
                }
                is StateHandler.Error -> {
                    hideLoading()
                    Toast.makeText(this, "Gagal masuk : ${it.message}", Toast.LENGTH_LONG).show()
                }
                is StateHandler.Success -> {
                    hideLoading()
                    Toast.makeText(this, "Berhasil masuk!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LandingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                else ->
                    Toast.makeText(this, "Gagal masuk : ", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun hideLoading() {
        binding.btnLogin.isEnabled = true
        binding.progressBarOnBtn.visibility = View.GONE
    }

    private fun showLoading() {
        binding.btnLogin.isEnabled = false
        binding.progressBarOnBtn.visibility = View.VISIBLE
    }

    private fun signUpPage() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun homePage() {
        val intent = Intent(this, LandingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}