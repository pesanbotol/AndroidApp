package com.pesanbotol.android.app.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.databinding.ActivitySignUpBinding
import com.pesanbotol.android.app.ui.landing.LandingActivity
import com.pesanbotol.android.app.utility.CommonFunction
import com.pesanbotol.android.app.utility.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {
    private val authViewModel by viewModel<AuthViewModel>()
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvLogin.setOnClickListener { loginPage() }
        binding.btnSignup.setOnClickListener {
            authViewModel.handleSignUp(
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
                    CommonFunction.showSnackBar(binding.root,applicationContext,"Gagal mendaftar : ${it.message}",true)
                }
                is StateHandler.Success -> {
                    hideLoading()
                    CommonFunction.showSnackBar(binding.root,applicationContext,"Berhasil mendaftar!")
                    val intent = Intent(this, LandingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
//                    onBackPressed()
                }

                else ->
                    CommonFunction.showSnackBar(binding.root,applicationContext,"Gagal mendaftar",true)
            }
        }

//        Utils.setTextColor(this, binding.iAgreeWith, 13, 18)
//        Utils.setTextColor(this, binding.iAgreeWith, 23, 30)
    }


    private fun hideLoading() {
        binding.btnSignup.isEnabled = true
        binding.progressBarOnBtn.visibility = View.GONE
    }

    private fun showLoading() {
        binding.btnSignup.isEnabled = false
        binding.progressBarOnBtn.visibility = View.VISIBLE
    }


    private fun loginPage() {
        onBackPressed()
    }

}