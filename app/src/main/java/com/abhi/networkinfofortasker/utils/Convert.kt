package com.abhi.networkinfofortasker.utils

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder


object Convert {
    fun bytes(context: Context, bytes: Long): String {
        return android.text.format.Formatter.formatFileSize(context, bytes)
    }

    /*fun packageNameToUid(context: Context, packageName: String): Int {
        return try {
            // tasker action plugin context might be incapable for querying uid, so as a workaround, asking UIDs directly
            val packageManager = context.applicationContext.packageManager
            packageManager.getApplicationInfo(packageName, 0).uid
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "packageNameToUid: failed to get uid of $packageName ${e.message}")
            e.printStackTrace()
            -1
        }
    }*/

    fun uidToAppPackageName(context: Context, uid: Int): String {
        return context.applicationContext.packageManager.getNameForUid(uid) ?: context.getString(
            android.R.string.unknownName
        )
    }

    fun convertToJson(output: Any): String {
        val gson = GsonBuilder().setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return gson.toJson(output)
    }
}