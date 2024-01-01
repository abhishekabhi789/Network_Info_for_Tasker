package com.abhi.networkinfofortasker.call

import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.IBinder
import android.provider.CallLog
import android.util.Log


class PhoneStateService : Service() {
    private val TAG = javaClass.simpleName
    private var callLogObserver: CallLogObserver? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Starting CallLogMonitoring")
        startCallLogObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Stopping CallLogMonitoring")
        stopCallLogObserver(this)
    }

    private fun startCallLogObserver(context: Context) {
        Log.d(TAG, "startCallLogObserver: attaching observer")
        callLogObserver = CallLogObserver(context, Handler())
        context.contentResolver.registerContentObserver(
            CallLog.Calls.CONTENT_URI,
            true,
            callLogObserver!!
        )
            }

    private fun stopCallLogObserver(context: Context) {
        Log.d(TAG, "stopCallLogObserver: detaching observer")
        context.contentResolver.unregisterContentObserver(callLogObserver as ContentObserver)
    }
}