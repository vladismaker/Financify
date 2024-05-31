package com.my.application.financify

import android.app.Application
import android.content.Context

class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        private lateinit var instance: AppContext

        val context: Context
            get() = instance.applicationContext
    }
}