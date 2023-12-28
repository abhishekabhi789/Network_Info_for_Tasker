package com.abhi.networkinfofortasker.wifi.actionnewwifi

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class NewWifiInput @JvmOverloads constructor(
    @field:TaskerInputField("wifi_ssid") val wifiSsid: String = "",
    @field:TaskerInputField("wifi_password") val wifiPassword: String? = null,
)
