package com.abhi.networkinfofortasker.call.eventcalllogentry

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.abhi.networkinfofortasker.R
import com.abhi.networkinfofortasker.call.CallLogObserver
import com.abhi.networkinfofortasker.utils.Dialogue.toToast
import com.abhi.networkinfofortasker.utils.PermissionHelper
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigNoInput


class CallLogEntryEventConfig : Activity(), TaskerPluginConfigNoInput {
    override val context: Context
        get() = applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val missingPermissions =
            PermissionHelper.getMissingPermissions(this, CallLogObserver.neededPermission)
        if (missingPermissions.isNotEmpty()) {
            PermissionHelper.askMissingPermissions(this, missingPermissions)
            finish()
        } else {
            this.getString(R.string.configuration_saved).toToast(this)
            CallLogEntryEventHelper(this).finishForTasker()
        }
    }
}