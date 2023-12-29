package com.abhi.networkinfofortasker.bluetooth.actionBluetoothTethering

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class ToggleBTTetherInput @JvmOverloads constructor(
    @field:TaskerInputField("turn_on_bt_tether") val shouldTurnOn:Boolean = true
)
