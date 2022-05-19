package com.pesanbotol.android.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.pesanbotol.android.app.data.auth.repository.SessionPreferenceRepository
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.bottle.repository.BottleRepository
import com.pesanbotol.android.app.data.bottle.viewmodel.BottleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class App : Application() {
    private val viewModelModules = module {
        viewModel {
            AuthViewModel(get(), this@App)
        }
        viewModel {
            BottleViewModel(get())
        }
    }
    private val repositoryModules = module {
        single { SessionPreferenceRepository.getInstance(dataStore) }
        single { BottleRepository() }
    }

    override fun onCreate() {
        FirebaseApp.initializeApp(this@App)
        super.onCreate()

        startKoin {
//            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    repositoryModules,
                    viewModelModules,
                )
            )
        }
    }
}