package stream.nolambda.karma.utils

import android.util.Log
import stream.nolambda.karma.Karma

object KarmaLogger {

    private const val TAG = "Karma"

    fun log(message: () -> String) {
        if (Karma.enableLog) {
            Log.d(TAG, message())
        }
    }
}