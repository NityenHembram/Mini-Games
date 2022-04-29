package com.amvlabs.minigames

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
               p0.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityStarted(p0: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityResumed(p0: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityPaused(p0: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityStopped(p0: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                TODO("Not yet implemented")
            }

            override fun onActivityDestroyed(p0: Activity) {
                TODO("Not yet implemented")
            }

        })
    }
}