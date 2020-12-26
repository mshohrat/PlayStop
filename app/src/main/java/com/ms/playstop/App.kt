package com.ms.playstop

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this.applicationContext).build()
        appContext = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")
    }

    companion object {

        private val TAG = "PLAYSTOP_APPLICATION"

        lateinit var appContext: Context
    }
}