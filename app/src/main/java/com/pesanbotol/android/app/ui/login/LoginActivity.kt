package com.pesanbotol.android.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.databinding.ActivityLoginBinding
import com.pesanbotol.android.app.ui.landing.LandingActivity
import com.pesanbotol.android.app.ui.signup.SignUpActivity
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val authViewModel by viewModel<AuthViewModel>()
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        authViewModel.authState.observe(this) { result ->
            when (result) {
                is StateHandler.Loading -> {
                    showLoading()
                }
                is StateHandler.Error -> {
                    hideLoading()
                    CommonFunction.showSnackBar(
                        binding.root,
                        applicationContext,
                        "Gagal masuk : ${result.message}",
                        true
                    )
                }
                is StateHandler.Success -> {
                    hideLoading()
                    CommonFunction.showSnackBar(binding.root, applicationContext, "Berhasil masuk!")
                    val intent = Intent(this, LandingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                else ->
                    CommonFunction.showSnackBar(
                        binding.root,
                        applicationContext,
                        "Gagal masuk",
                        true
                    )
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

    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun homePage() {
        val intent = Intent(this, LandingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when (view?.id) {
            R.id.tv_signup -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_login -> {
                when {
                    TextUtils.isEmpty(email) -> {
                        binding.emailEditText.error = "Field ini tidak boleh kosong"
                    }
                    TextUtils.isEmpty(password) -> {
                        binding.passwordTextInput.error = "Field ini tidak boleh kosong"
                    }
                    else -> {
                        authViewModel.handleSignIn(
                            email = email,
                            password = password
                        )
                    }
                }
            }
        }
    }
}