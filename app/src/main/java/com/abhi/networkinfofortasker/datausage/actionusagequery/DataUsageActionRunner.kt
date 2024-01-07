package com.abhi.networkinfofortasker.datausage.actionusagequery

import android.app.usage.NetworkStats.Bucket
import android.content.Context
import android.net.ConnectivityManager
import com.abhi.networkinfofortasker.NetworkType
import com.abhi.networkinfofortasker.datausage.DataUsage
import com.abhi.networkinfofortasker.datausage.DataUsageQuery
import com.abhi.networkinfofortasker.datausage.DataUsageQuery.CustomKeys
import com.abhi.networkinfofortasker.siminfo.SimInfoQuery
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
    private lateinit var mContext: Context
    private lateinit var networkStatManager: DataUsageQuery
    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var networkType: Int = -1
    private var slotIndex: Int? = null
    override fun run(
        context: Context, input: TaskerInput<DataUsageActionInput>
    ): TaskerPluginResult<DataUsageActionOutput> {
        mContext = context
        val missingPermission =
            DataUsageQuery.requiredPermissions.filterNot { it.hasPermission(context) }
                .map { it.errorMessage }

        if (missingPermission.isNotEmpty()) {
            return TaskerPluginResultErrorWithOutput(
                1, missingPermission.joinToString("|")
            )
        }
        startTime = when (input.regular.queryMode) {
            DataUsageQuery.QueryMode.TODAY.id -> getStartTimeForToday()
            DataUsageQuery.QueryMode.THIS_MONTH.id -> getStartTimeForThisMonth()
            else -> input.regular.startTime?.toLong() ?: System.currentTimeMillis()
        }
        endTime = input.regular.endTime?.toLong() ?: System.currentTimeMillis()
        networkType = when (input.regular.networkType) {
            NetworkType.MOBILE.id -> ConnectivityManager.TYPE_MOBILE
            NetworkType.WIFI.id -> ConnectivityManager.TYPE_WIFI
            else -> -1
        }
        slotIndex = if (input.regular.networkType == NetworkType.MOBILE.id) {
            when (input.regular.chosenSim) {
                SimSlots.SIM1.id -> SimSlots.SIM1.index
                SimSlots.SIM2.id -> SimSlots.SIM2.index
                SimSlots.DEFAULT_DATA.id -> SimInfoQuery().getDataSimOperator(context).slotIndex - 1
                else -> null
            }
        } else null

        networkStatManager = DataUsageQuery()
        val dataMap = mutableMapOf<String, Any>()
        //stage1 querying device data usage
        val deviceUsageBucket = networkStatManager.getDeviceUsage(
            context, startTime, endTime, networkType, slotIndex
        )
        val deviceDataUsage = getDataUsageObj(deviceUsageBucket)
        dataMap[CustomKeys.DEVICE.getKey()] = deviceDataUsage
        //stage2 querying custom uid data usage
        input.regular.apply {
            if (uidAll) dataMap[CustomKeys.ALL.getKey()] = getUidUsage(Bucket.UID_ALL)
            if (uidRemoved) dataMap[CustomKeys.REMOVED.getKey()] = getUidUsage(Bucket.UID_REMOVED)
            if (uidTethering) dataMap[CustomKeys.TETHERING.getKey()] =
                getUidUsage(Bucket.UID_TETHERING)
        }
        val uidList: MutableList<Int> = mutableListOf()
        //stage3 querying app data usage
        if (!input.regular.appPackages.isNullOrEmpty()) {
            val packages = input.regular.appPackages!!.split(",")
            uidList.addAll(packages.mapNotNull { it.trim().toIntOrNull() })
            val uidUsageList = mutableListOf<DataUsage.AppUsage>()
            for (uid in uidList) {
                val appPackageName = Convert.uidToAppPackageName(context, uid)
                val uidUsage = DataUsage.AppUsage(uid, appPackageName, getUidUsage(uid))
                uidUsageList.add(uidUsage)
            }
            uidUsageList.sortByDescending { it.dataUsage.total }
            dataMap["apps"] = uidUsageList
        }
        val dataUsageJson = Convert.convertToJson(dataMap)
        return TaskerPluginResultSucess(DataUsageActionOutput(dataUsageJson))
    }

    private fun getDataUsageObj(bucket: Bucket): DataUsage {
        val bytesUp = bucket.txBytes
        val bytesDown = bucket.rxBytes
        val bytesTotal = bytesUp + bytesDown
        return DataUsage(
            bytesUp, Convert.bytes(mContext, bytesUp),
            bytesDown, Convert.bytes(mContext, bytesDown),
            bytesTotal, Convert.bytes(mContext, bytesTotal)
        )
    }

    private fun getUidUsage(uid: Int): DataUsage {
        val uidBucket = networkStatManager.getUidUsage(
            mContext, uid, startTime, endTime, networkType, slotIndex
        )
        return getDataUsageObj(uidBucket)
    }
}