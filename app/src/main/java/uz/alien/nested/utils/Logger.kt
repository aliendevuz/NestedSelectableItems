package uz.alien.nested.utils

import android.util.Log
import uz.alien.nested.BuildConfig

object Logger {

    const val LOGGING_IS_AVAILABLE = true

    fun logToLogcat(level: String, tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            if (LOGGING_IS_AVAILABLE) {
                when (level.lowercase()) {
                    "debug" -> Log.d(tag, msg)
                    "info" -> Log.i(tag, msg)
                    "warn" -> Log.w(tag, msg)
                    "error" -> Log.e(tag, msg)
                }
            }
        }
    }

    fun d(tag: String, msg: String) {
        log("DEBUG", tag, msg)
    }

    fun i(tag: String, msg: String) {
        log("INFO", tag, msg)
    }

    fun w(tag: String, msg: String) {
        log("WARN", tag, msg)
    }

    fun e(tag: String, msg: String) {
        log("ERROR", tag, msg)
    }

    private fun log(level: String, tag: String, msg: String) {
        try {
            logToLogcat(level, tag, msg)
        } catch (_: Exception) {
            println("$level/$tag: $msg")
        }
    }
}