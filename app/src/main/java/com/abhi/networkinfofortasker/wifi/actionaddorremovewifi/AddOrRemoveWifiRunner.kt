package com.abhi.networkinfofortasker.wifi.actionaddorremovewifi

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultErrorWithOutput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess


class AddOrRemoveWifiRunner : TaskerPluginRunnerActionNoOutput<AddOrRemoveWifiInput>() {
    override fun run(
        context: Context,
        input: TaskerInput<AddOrRemoveWifiInput>
    ): TaskerPluginResult<Unit> {
        val status = WifiHelper().addOrRemoveNetwork(
            context,
            input.regular.wifiSsid,
            input.regular.wifiPassword,
            input.regular.shouldRemove
        )
        return when (status) {
            WifiHelper.ActionStatus.SUCCESS -> TaskerPluginResultSucess()
            WifiHelper.ActionStatus.FAILED -> TaskerPluginResultErrorWithOutput(
                1,
                WifiHelper.ActionStatus.FAILED.status
            )

            WifiHelper.ActionStatus.WIFI_DISABLED -> TaskerPluginResultErrorWithOutput(
                2,
                WifiHelper.ActionStatus.WIFI_DISABLED.status
            )

            WifiHelper.ActionStatus.UNKNOWN -> TaskerPluginResultErrorWithOutput(
                3,
                WifiHelper.ActionStatus.UNKNOWN.status
            )
        }
    }
}