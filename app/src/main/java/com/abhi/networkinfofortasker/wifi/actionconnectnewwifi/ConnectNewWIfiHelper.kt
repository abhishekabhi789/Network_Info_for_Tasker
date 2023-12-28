package com.abhi.networkinfofortasker.wifi.actionconnectnewwifi

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutput

class ConnectNewWIfiHelper(config: TaskerPluginConfig<ConnectNewWifiInput>) :
    TaskerPluginConfigHelperNoOutput<ConnectNewWifiInput, ConnectNewWifiRunner>(config) {
    override val inputClass: Class<ConnectNewWifiInput>
        get() = ConnectNewWifiInput::class.java
    override val runnerClass: Class<ConnectNewWifiRunner>
        get() = ConnectNewWifiRunner::class.java
}