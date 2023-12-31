package com.abhi.networkinfofortasker.datausage.actionusagequery

import android.content.Context
import android.net.ConnectivityManager
import com.abhi.networkinfofortasker.NetworkType
import com.abhi.networkinfofortasker.datausage.DataUsage
import com.abhi.networkinfofortasker.datausage.DataUsageQuery
import com.abhi.networkinfofortasker.siminfo.SimSlots
import com.abhi.networkinfofortasker.utils.Convert
import com.abhi.networkinfofortasker.utils.TimeUtils.getStartTimeForThisMonth
import com.abhi.networkinfofortasker.utils.TimeUtils.getStartTimeForToday
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultErrorWithOutput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess

class DataUsageActionRunner :
    TaskerPluginRunnerAction<DataUsageActionInput, DataUsageActionOutput>() {
    override fun run(
        context: Context,
        input: TaskerInput<DataUsageActionInput>
    ): TaskerPluginResult<DataUsageActionOutput> {
        val missingPermission = DataUsageQuery.requiredPermissions
            .filterNot { it.hasPermission(context) }
            .map { it.errorMessage }

        if (missingPermission.isNotEmpty()) {
            return TaskerPluginResultErrorWithOutput(
                1,
                missingPermission.joinToString("|")
            )
        }
        val startTime: Long = when (input.regular.queryMode) {
            DataUsageQuery.QueryMode.TODAY.id -> getStartTimeForToday()
            DataUsageQuery.QueryMode.THIS_MONTH.id -> getStartTimeForThisMonth()
            else -> input.regular.startTime!!.toLong()
        }
        val endTime: Long =
            if (input.regular.endTime != null) input.regular.endTime!!.toLong() else System.currentTimeMillis()
        val networkType: Int = when (input.regular.networkType) {
            NetworkType.MOBILE.id -> ConnectivityManager.TYPE_MOBILE
            NetworkType.WIFI.id -> ConnectivityManager.TYPE_WIFI
            else -> -1
        }
        val slotIndex: Int? =
            if (input.regular.networkType == NetworkType.MOBILE.id) {
                when (input.regular.chosenSim) {
                    SimSlots.SIM1.id -> SimSlots.SIM1.index

                    SimSlots.SIM2.id -> SimSlots.SIM2.index
                    else -> null
                }
            } else null

        val networkStatManager = DataUsageQuery()
        if (input.regular.appPackages.isNullOrEmpty()) {
            val (bytesUp, bytesDown) = networkStatManager.getDeviceUsage(
                context,
                startTime,
                endTime,
                networkType,
                slotIndex
            )
            val bytesTotal = bytesUp + bytesDown
            val dataUsageJson = DataUsage(
                bytesUp.toString(),
                Convert.bytes(context, bytesUp),
                bytesDown.toString(),
                Convert.bytes(context, bytesDown),
                bytesTotal.toString(),
                Convert.bytes(context, bytesTotal)
            )
            return TaskerPluginResultSucess(
                DataUsageActionOutput(Convert.convertToJson(dataUsageJson))
            )
        } else {
            val uidList = input.regular.appPackages!!.split(",").map { it.trim().toInt() }
            val dataMap = mutableMapOf<String, DataUsage>()
            for (uid in uidList) {
                val (bytesUp, bytesDown) = networkStatManager.getUidUsage(
                    context,
                    uid,
                    startTime,
                    endTime,
                    networkType,
                    slotIndex
                )
                val bytesTotal = bytesUp + bytesDown
                val data = DataUsage(
                    bytesUp.toString(),
                    Convert.bytes(context, bytesUp),
                    bytesDown.toString(),
                    Convert.bytes(context, bytesDown),
                    bytesTotal.toString(),
                    Convert.bytes(context, bytesTotal)
                )
                val appName = Convert.uidToAppName(context, uid) ?: uid.toString()
                dataMap[appName] = data
            }
            return TaskerPluginResultSucess(
                DataUsageActionOutput(Convert.convertToJson(dataMap))
            )
        }
    }
}