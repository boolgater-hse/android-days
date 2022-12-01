package com.studyandroid.vkalbums

import android.app.Application
import android.content.Context
import com.studyandroid.vkalbums.data.remote.WifiService

class VKAlbumsApp : Application() {

    companion object {

        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        setupServices()
    }

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
    }
}
