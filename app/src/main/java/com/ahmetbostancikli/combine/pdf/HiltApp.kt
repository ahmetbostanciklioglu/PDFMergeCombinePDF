package com.ahmetbostancikli.combine.pdf

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//Hilt Application Class is created to do dagger hilt in Application level
@HiltAndroidApp
class HiltApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}