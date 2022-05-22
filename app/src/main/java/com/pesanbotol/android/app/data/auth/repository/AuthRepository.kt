package com.pesanbotol.android.app.data.auth.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class AuthRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

}