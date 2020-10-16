package com.recipe.app

import android.app.Application
import com.blankj.utilcode.util.Utils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)

    }
    companion object {
        lateinit var instance: App
            private set
    }
}