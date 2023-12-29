package com.abhi.networkinfofortasker.bluetooth.actionBluetoothTethering

import android.content.Context
import com.abhi.networkinfofortasker.bluetooth.BluetoothTetheringHelper
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess


class ToggleBTTetheringRunner : TaskerPluginRunnerActionNoOutput<ToggleBTTetherInput>() {
    override fun run(
        context: Context,
        input: TaskerInput<ToggleBTTetherInput>
    ): TaskerPluginResult<Unit> {
        BluetoothTetheringHelper(context).toggleTethering(input.regular.shouldTurnOn)
        return TaskerPluginResultSucess()
    }

}