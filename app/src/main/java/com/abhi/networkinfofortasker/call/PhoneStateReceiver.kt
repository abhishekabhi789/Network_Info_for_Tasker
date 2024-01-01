package com.abhi.networkinfofortasker.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log


class PhoneStateReceiver : BroadcastReceiver() {
    private val TAG = javaClass.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: ${intent?.action}")
        if (intent != null) {
            when (intent.action) {
                TelephonyManager.ACTION_PHONE_STATE_CHANGED -> startService(context)
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> dealWithAirplaneMode(context)
                Intent.ACTION_BOOT_COMPLETED -> startService(context)
                Intent.ACTION_MY_PACKAGE_REPLACED -> startService(context)
            }
        }
    }

    private fun startService(context: Context?) {
        Log.d(TAG, "startService: starting service")
        val serviceIntent = Intent(context, PhoneStateService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }
    }

    private fun stopService(context: Context?) {
        Log.d(TAG, "stopService: stopping service")
        context?.stopService(Intent(context, PhoneStateService::class.java))
    }

    private fun isOnAirplane(context: Context?): Boolean {
        return Settings.System.getInt(
            context?.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }

    private fun dealWithAirplaneMode(context: Context?) {
        if (isOnAirplane(context)) stopService(context) else startService(context)

    }
}
