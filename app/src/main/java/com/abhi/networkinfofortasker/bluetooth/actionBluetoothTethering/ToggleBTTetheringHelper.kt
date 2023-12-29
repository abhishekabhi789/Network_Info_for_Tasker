package com.abhi.networkinfofortasker.bluetooth.actionBluetoothTethering

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutput

class ToggleBTTetheringHelper(config: TaskerPluginConfig<ToggleBTTetherInput>) :
    TaskerPluginConfigHelperNoOutput<ToggleBTTetherInput, ToggleBTTetheringRunner>(config) {
    override val inputClass: Class<ToggleBTTetherInput>
        get() = ToggleBTTetherInput::class.java
    override val runnerClass: Class<ToggleBTTetheringRunner>
        get() = ToggleBTTetheringRunner::class.java
}