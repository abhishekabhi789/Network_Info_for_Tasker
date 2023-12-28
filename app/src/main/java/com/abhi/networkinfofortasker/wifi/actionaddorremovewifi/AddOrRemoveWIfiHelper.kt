package com.abhi.networkinfofortasker.wifi.actionaddorremovewifi

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutput

class AddOrRemoveWIfiHelper(config: TaskerPluginConfig<AddOrRemoveWifiInput>) :
    TaskerPluginConfigHelperNoOutput<AddOrRemoveWifiInput, AddOrRemoveWifiRunner>(config) {
    override val inputClass: Class<AddOrRemoveWifiInput>
        get() = AddOrRemoveWifiInput::class.java
    override val runnerClass: Class<AddOrRemoveWifiRunner>
        get() = AddOrRemoveWifiRunner::class.java
}