package com.ms.playstop

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.messaging.FirebaseMessaging
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.orhanobut.hawk.Hawk


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this.applicationContext).build()
        appContext = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")
        AppCenter.start(
            this, BuildConfig.APP_CENTER_CLIENT,
            Analytics::class.java, Crashes::class.java
        )
    }

    companion object {

        private val TAG = "PLAYSTOP_APPLICATION"

        lateinit var appContext: Context
    }
}