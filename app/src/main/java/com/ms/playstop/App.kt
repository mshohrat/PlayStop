package com.ms.playstop

import android.app.Application
import android.content.Context
import com.orhanobut.hawk.Hawk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this.applicationContext).build()
        appContext = this
    }

    companion object {

        private val TAG = "PLAYSTOP_APPLICATION"

        lateinit var appContext: Context
    }
}