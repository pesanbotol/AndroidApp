package com.pesanbotol.android.app.data.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.pesanbotol.android.app.data.auth.model.User
import com.pesanbotol.android.app.data.auth.repository.SessionPreferenceRepository
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.utility.isValidEmail
import kotlinx.coroutines.launch

class AuthViewModel(
    private val sessionPreferenceRepository: SessionPreferenceRepository,
    application: Application
) :
    ViewModel() {

    init {
        FirebaseApp.initializeApp(application)
    }

    private val _authState = MutableLiveData<StateHandler<AuthResult>>()
    private val _authStatusState = MutableLiveData<Boolean>()
    val authState: LiveData<StateHandler<AuthResult>> = _authState
    val authStatusState: LiveData<Boolean> = _authStatusState

    private var _auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun isLoggedIn(): Boolean {
        return if (_auth.currentUser != null) {
            _authStatusState.postValue(true)
            true
        } else {
            _authStatusState.postValue(false)
            false
        }
    }

    fun firebaseUser(): FirebaseUser? = _auth.currentUser

    fun isLoggedInOnRealtime() = sessionPreferenceRepository.getUser().asLiveData()

    fun loggedOut() {
        viewModelScope.launch {
            _auth.signOut()
            sessionPreferenceRepository.removeUser()
            sessionPreferenceRepository.removeToken()
        }.isCompleted.apply {
            _authStatusState.postValue(false)
        }
    }

    fun getPassedOnboarding() =
            sessionPreferenceRepository.getOnboardingStatus().asLiveData()

    fun savePassedOnboarding() {
        viewModelScope.launch {
            sessionPreferenceRepository.passedOnboarding()
        }
    }

    fun handleSignUp(email: String, password: String) {
        if (!email.isValidEmail()) {
            _authState.postValue(StateHandler.Error("Invalid email"))
            return
        }
//        if (password != confirmPassword) {
//            _authState.postValue(StateHandler.Error("Password does not match"))
//            return
        _authState.postValue(StateHandler.Loading())
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(AuthViewModel::class.java.simpleName, "Email signup is successful")
                    _authState.postValue(StateHandler.Success(task.result))
                } else {
                    task.exception?.let {
                        Log.i(
                            AuthViewModel::class.java.simpleName,
                            "Email signup failed with error ${it.localizedMessage}"
                        )
                        _authState.postValue(StateHandler.Error(it.localizedMessage))
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
                    _authStatusState.postValue(true)
                    viewModelScope.launch {
                        task.result?.user?.let {
                            sessionPreferenceRepository.saveUser(
                                User(
                                    name = it.displayName,
                                    uid = it.uid,
                                    photoUrl = it.photoUrl?.path,
                                    email = it.email
                                )
                            )
                        }

                    }
                } else {
                    _authStatusState.postValue(false)

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