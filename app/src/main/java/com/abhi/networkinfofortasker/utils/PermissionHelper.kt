package com.abhi.networkinfofortasker.utils

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.abhi.networkinfofortasker.utils.Dialogue.toToast


object PermissionHelper {
    private fun hasAppOpsPermission(context: Context, permission: String): Boolean {
        return try {
            val packageManager: PackageManager = context.packageManager
            val applicationInfo =
                packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                permission,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openPermissionSettingsPage(context: Context, permission: String) {
        val intent = Intent(permission)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

    private fun checkPermissionWithContextCompat(
        context: Context,
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askPermissionWithActivityCompat(
        context: Any,
        permissions: String
    ) {
        when (context) {
            is Activity -> ActivityCompat.requestPermissions(
                context,
                arrayOf(permissions),
                123
            )

            is Context -> launchAppSettings(context)
        }
    }

    fun getMissingPermissions(
        context: Context,
        requiredPermissions: List<Permission>
    ): MutableList<Permission> {

        val missingPermissions = mutableListOf<Permission>()
        for (permission in requiredPermissions) {
            if (!permission.checkPermission(context)) {
                missingPermissions.add(permission)
            }
        }
        return missingPermissions
    }

    fun askMissingPermissions(context: Context, missingPermissions: List<Permission>): String {
        for (permission in missingPermissions) {
            "Error: ${permission.errorMessage}".toToast(context)
            permission.requestPermission(context)
        }
        return missingPermissions.joinToString(", ") { it.errorMessage }
    }

    private fun launchAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    enum class Permission(
        val errorMessage: String,
        val checkPermission: (Context) -> Boolean,
        val requestPermission: (Any) -> Unit
    ) {
        USAGE_ACCESS(
            "missing usage access",
            { context -> hasAppOpsPermission(context, AppOpsManager.OPSTR_GET_USAGE_STATS) },
            { context ->
                openPermissionSettingsPage(
                    context as Context,
                    Settings.ACTION_USAGE_ACCESS_SETTINGS
                )
            }
        ),
        PHONE_STATE(
            "missing phone access",
            { context ->
                checkPermissionWithContextCompat(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                )
            },
            { context ->
                askPermissionWithActivityCompat(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                )
            }
        ),
        WRITE_SETTINGS("missing write settings permission", { context ->
            Settings.System.canWrite(context)
        }, { context ->
            openPermissionSettingsPage(
                context as Context,
                Settings.ACTION_MANAGE_WRITE_SETTINGS
            )
        })

    }
}