package de.kb1000.flashlight

import android.app.Application
import timber.log.Timber

class FlashlightApplication : Application() {
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
