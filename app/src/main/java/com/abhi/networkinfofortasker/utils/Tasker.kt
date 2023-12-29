package com.abhi.networkinfofortasker.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast
import com.abhi.networkinfofortasker.R
import com.abhi.networkinfofortasker.bluetooth.actionBluetoothTethering.ToggleBTTetheringHelper
import com.abhi.networkinfofortasker.datausage.actionusagequery.DataUsageActionHelper
import com.abhi.networkinfofortasker.siminfo.actioninfoquery.SimInfoActionHelper
import com.abhi.networkinfofortasker.utils.Dialogue.showSingleChoiceDialog
import com.abhi.networkinfofortasker.utils.Dialogue.toToast
import com.abhi.networkinfofortasker.wifi.actionaddorremovewifi.AddOrRemoveWIfiHelper

object Tasker {
    /**Save Action configuration after validating*/
    fun saveConfig(
        activity: Activity,
        taskerHelper: Any,
        requiredPermissions: List<PermissionHelper.Permission>?
    ) {
        if (requiredPermissions != null) {
            val missingPermissions =
                PermissionHelper.getMissingPermissions(activity, requiredPermissions)
            if (missingPermissions.isNotEmpty()) {
                PermissionHelper.askMissingPermissions(activity, missingPermissions)
                return
            }
        }
        val helper = when (taskerHelper) {
            is DataUsageActionHelper -> taskerHelper
            is SimInfoActionHelper -> taskerHelper
            is AddOrRemoveWIfiHelper -> taskerHelper
            is ToggleBTTetheringHelper -> taskerHelper
            else -> return
        }
        if (helper.onBackPressed().success) {
            helper.finishForTasker()
            Toast.makeText(activity, R.string.configuration_saved, Toast.LENGTH_SHORT).show()
        } else Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
    }

    /**Helps to choose a tasker variable*/
    fun chooseVariable(activity: Activity, field: Int, taskerHelper: Any) {
        val helper = when (taskerHelper) {
            is DataUsageActionHelper -> taskerHelper
            is SimInfoActionHelper -> taskerHelper
            is AddOrRemoveWIfiHelper -> taskerHelper
            else -> return
        }
        val relevantVariables = helper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) {
            activity.getString(R.string.no_variable_to_show).toToast(activity)
            return
        }
        activity.showSingleChoiceDialog(
            activity.getString(R.string.choose_a_variable),
            relevantVariables
        ) {
            if (!it.isNullOrEmpty()) activity.findViewById<EditText>(field).setText(it)
        }
    }
}