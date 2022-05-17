package com.pesanbotol.android.app.data.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pesanbotol.android.app.data.auth.repository.SessionPreferenceRepository
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.utility.isValidEmail
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class AuthViewModel(
    private val sessionPreferenceRepository: SessionPreferenceRepository,
    application: Application
) :
    ViewModel() {
    init {
        FirebaseApp.initializeApp(application)
    }

    private val _onboardingStatus = MutableLiveData<Boolean>()
    private val _authState = MutableLiveData<StateHandler<AuthResult>>()
    val authState :LiveData<StateHandler<AuthResult>> = _authState
    val onboardingStatus: LiveData<Boolean> = _onboardingStatus

    private var _auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun isLoggedIn(): Boolean {
        return _auth.currentUser != null
    }

    fun getPassedOnboarding() =
            sessionPreferenceRepository.getOnboardingStatus().asLiveData()

    fun savePassedOnboarding() {
        viewModelScope.launch {
            sessionPreferenceRepository.passedOnboarding()
        }
    }

    fun handleSignUp(email: String, password: String, confirmPassword: String) {
        if (!email.isValidEmail()) {
            _authState.value = StateHandler.Error("Invalid email")
            return
        }
        if (password != confirmPassword) {
            _authState.value = StateHandler.Error("Password does not match")
            return
        }
        _authState.value = StateHandler.Loading()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(AuthViewModel::class.java.simpleName, "Email signup is successful")
                    _authState.value = StateHandler.Success(task.result)
                } else {
                    task.exception?.let {
                        Log.i(
                            AuthViewModel::class.java.simpleName,
                            "Email signup failed with error ${it.localizedMessage}"
                        )
                        _authState.value = StateHandler.Error(it.localizedMessage)
                    }
                }
            }
    }

    fun handleSignIn(email: String, password: String) {
        if (!email.isValidEmail()) {
            _authState.postValue(StateHandler.Error("Invalid email"))
            return
        }
//        if (password != confirmPassword) {
//            _authState.value = StateHandler.Error("Password does not match")
//            return
//        }
        _authState.postValue(StateHandler.Loading())
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(AuthViewModel::class.java.simpleName, "Login berhasil!")
                    _authState.postValue(StateHandler.Success(task.result))
                    viewModelScope.launch {
                        task.result?.user?.let { sessionPreferenceRepository.saveUser(it) }

                    }
                } else {
                    task.exception?.let {
                        Log.i(
                            AuthViewModel::class.java.simpleName,
                            "Login gagal : ${it.localizedMessage}"
                        )
                        _authState.postValue(StateHandler.Error(it.localizedMessage))
                    }
                }
            }
    }


//    fun getUser(): FirebaseUser? {
//        viewModelScope.launch {
//            return sessionPreferenceRepository.getUser().singleOrNull()
//        }
//    }
}