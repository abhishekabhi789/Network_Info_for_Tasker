package com.abhi.networkinfofortasker.wifi.actionaddorremovewifi

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class AddOrRemoveWifiInput @JvmOverloads constructor(
    @field:TaskerInputField("wifi_ssid") val wifiSsid: String = "",
    @field:TaskerInputField("wifi_password") val wifiPassword: String? = null,
    @field:TaskerInputField("remove_network") val shouldRemove:Boolean = false
)
