package com.abhi.networkinfofortasker.wifi.actionnewwifi

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutput

class NewWIfiHelper(config: TaskerPluginConfig<NewWifiInput>) :
    TaskerPluginConfigHelperNoOutput<NewWifiInput, NewWifiRunner>(config) {
    override val inputClass: Class<NewWifiInput>
        get() = NewWifiInput::class.java
    override val runnerClass: Class<NewWifiRunner>
        get() = NewWifiRunner::class.java
}