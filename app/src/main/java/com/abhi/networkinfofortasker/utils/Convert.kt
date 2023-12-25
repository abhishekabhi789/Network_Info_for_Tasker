package com.abhi.networkinfofortasker.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.google.gson.Gson

object Convert {
    private val TAG = javaClass.simpleName
    fun bytes(context: Context, bytes: Long): String {
        return android.text.format.Formatter.formatFileSize(context, bytes)
    }

    /*fun packageNameToUid(context: Context, packageName: String): Int {
        return try {
            // tasker action plugin context might me be incapable for querying uid, so as a workaround, asking UIDs directly
            val packageManager = context.applicationContext.packageManager
            packageManager.getApplicationInfo(packageName, 0).uid
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "packageNameToUid: failed to get uid of $packageName ${e.message}")
            e.printStackTrace()
            -1
        }
    }*/

    fun uidToAppName(context: Context, uid: Int): String? {
        return context.applicationContext.packageManager.getNameForUid(uid)
    }

    fun convertToJson(output: Any): String {
        return Gson().toJson(output)
    }
}