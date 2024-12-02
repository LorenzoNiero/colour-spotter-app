package com.challenge.colour_spotter

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

    }

}