package com.androiddevs.mvvmnewsapp.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    private var currentAct: Activity? = null

    companion object {
        lateinit var myAppContext: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate();
        myAppContext = this.applicationContext as MyApplication
        actInstance()
    }
    fun getCurrentAct(): Activity? {
        return currentAct
    }
    private fun actInstance() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
                currentAct = p0
            }

            override fun onActivityResumed(p0: Activity) {
                currentAct = p0
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
//                if (currentAct == activity) {
//                    currentAct = null
//                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        })
    }

}
