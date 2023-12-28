package com.abhi.networkinfofortasker.wifi.actionnewwifi

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess


class NewWifiRunner : TaskerPluginRunnerActionNoOutput<NewWifiInput>() {
    override fun run(
        context: Context,
        input: TaskerInput<NewWifiInput>
    ): TaskerPluginResult<Unit> {
        WifiHelper().connectNewWiFi(
            context,
            input.regular.wifiSsid,
            input.regular.wifiPassword
        )
        return TaskerPluginResultSucess()
    }
}