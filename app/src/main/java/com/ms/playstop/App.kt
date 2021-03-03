package com.ms.playstop

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.firebase.messaging.FirebaseMessaging
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.ui.settings.SettingsFragment
import com.orhanobut.hawk.Hawk


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this.applicationContext).build()
        SettingsFragment.initDarkModeFromSetting()
        appContext = this
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
