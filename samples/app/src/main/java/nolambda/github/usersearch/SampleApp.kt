package nolambda.github.usersearch

import android.app.Application
import stream.nolambda.karma.Karma

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Karma.enableLog = BuildConfig.DEBUG
    }
}