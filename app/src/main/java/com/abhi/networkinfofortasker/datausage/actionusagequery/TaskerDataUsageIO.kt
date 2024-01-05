package com.abhi.networkinfofortasker.datausage.actionusagequery

import com.abhi.networkinfofortasker.NetworkType
import com.abhi.networkinfofortasker.R
import com.abhi.networkinfofortasker.datausage.DataUsageQuery
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerInputRoot
class DataUsageActionInput @JvmOverloads constructor(
    @field:TaskerInputField("queryMode") val queryMode: String = DataUsageQuery.QueryMode.TODAY.id,
    @field:TaskerInputField("networkType") val networkType: String = NetworkType.MOBILE.id,
    @field:TaskerInputField("chosenSim") val chosenSim: String? = null,
    @field:TaskerInputField("startTime") val startTime: String? = null,
    @field:TaskerInputField("endTime") val endTime: String? = null,
    @field:TaskerInputField("appPackages") val appPackages: String? = null,
    @field:TaskerInputField("uid_all") val uidAll: Boolean = false,
    @field:TaskerInputField("uid_removed") val uidRemoved: Boolean = false,
    @field:TaskerInputField("uid_tethering") val uidTethering: Boolean = false

)

@TaskerInputRoot
@TaskerOutputObject()
class DataUsageActionOutput @JvmOverloads constructor(
    @field:TaskerInputField("data_usage") @get:TaskerOutputVariable(
        "data_usage",
        R.string.du_label_data_usage,
        R.string.du_html_label_data_usage
    ) val dataUsage: String? = null
)
