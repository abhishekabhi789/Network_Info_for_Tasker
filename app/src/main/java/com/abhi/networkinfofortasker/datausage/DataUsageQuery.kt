package com.abhi.networkinfofortasker.datausage

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.os.RemoteException
import com.abhi.networkinfofortasker.utils.PermissionHelper


class DataUsageQuery {

    fun getDeviceUsage(
        context: Context, startTime: Long, endTime: Long, networkType: Int, slotIndex: Int?
    ): Pair<Long, Long> {
        val bucket: NetworkStats.Bucket
        val networkStatsManager = context.getSystemService(NetworkStatsManager::class.java)
        var totalUp = 0L
        var totalDown = 0L
        try {
            val subscriberId = getSubscriberIdForSim(context, slotIndex)
            bucket = networkStatsManager.querySummaryForDevice(
                networkType, subscriberId, startTime, endTime
            )
            totalUp += bucket.txBytes
            totalDown += bucket.rxBytes
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return Pair(totalUp, totalDown)
    }

    fun getUidUsage(
        context: Context,
        uid: Int,
        startTime: Long,
        endTime: Long,
        networkType: Int,
        slotIndex: Int?
    ): Pair<Long, Long> {
        val networkStats: NetworkStats
        val networkStatsManager = context.getSystemService(NetworkStatsManager::class.java)
        var totalUp = 0L
        var totalDown = 0L
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                networkType, getSubscriberIdForSim(context, slotIndex), startTime, endTime, uid
            )
            val bucket = NetworkStats.Bucket()
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket)
                totalUp += bucket.txBytes
                totalDown += bucket.rxBytes
            }
            networkStats.close()

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return Pair(totalUp, totalDown)
    }

    companion object {
        val requiredPermissions = listOf(
            PermissionHelper.Permission.USAGE_ACCESS,
            PermissionHelper.Permission.PHONE_STATE
        )
    }

    enum class QueryMode(val id: String) {
        TODAY("query_mode_today"), THIS_MONTH("query_mode_this_month"), CUSTOM("query_mode_custom_time")
    }

}