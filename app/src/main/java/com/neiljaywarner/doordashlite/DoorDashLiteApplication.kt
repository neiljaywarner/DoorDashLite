package com.neiljaywarner.doordashlite

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.github.ajalt.timberkt.d
import timber.log.Timber

class DoorDashLiteApplication : Application(), Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {
        if (activity == null) {
            return
        }
        d { "${activity::class.java.simpleName} paused" }
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity == null) {
            return
        }
        d { "${activity::class.java.simpleName} created" }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        registerActivityLifecycleCallbacks(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        //TODO: Write out via descriptions; this is just in here as a stub of something to do in production
        d { "onTrimMemory: $level"}
    }



}