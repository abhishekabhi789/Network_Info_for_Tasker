package com.abhi.networkinfofortasker.datausage

import android.app.usage.NetworkStats
import android.app.usage.NetworkStats.Bucket
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.os.RemoteException
import com.abhi.networkinfofortasker.utils.PermissionHelper


class DataUsageQuery {

    fun getDeviceUsage(
        context: Context, startTime: Long, endTime: Long, networkType: Int, slotIndex: Int?
    ): Bucket {
        var bucket = Bucket()
        val networkStatsManager = context.getSystemService(NetworkStatsManager::class.java)
        try {
            val subscriberId = getSubscriberIdForSim(context, slotIndex)
            bucket = networkStatsManager.querySummaryForDevice(
                networkType, subscriberId, startTime, endTime
            )
        } catch (e1: RemoteException) {
            e1.printStackTrace()
        } catch (e2: SecurityException) {
            e2.printStackTrace()
        }
        return bucket
    }

    fun getUidUsage(
        context: Context,
        uid: Int,
        startTime: Long,
        endTime: Long,
        networkType: Int,
        slotIndex: Int?
    ): Bucket {
        val networkStats: NetworkStats
        val networkStatsManager = context.getSystemService(NetworkStatsManager::class.java)
        val bucket = Bucket()
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                networkType, getSubscriberIdForSim(context, slotIndex), startTime, endTime, uid
            )
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket)

            }
            networkStats.close()

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return bucket
    }

    companion object {
        val requiredPermissions = listOf(
            PermissionHelper.Permission.USAGE_ACCESS,
            PermissionHelper.Permission.READ_PHONE_STATE
        )
    }

    enum class QueryMode(val id: String) {
        TODAY("query_mode_today"), THIS_MONTH("query_mode_this_month"), CUSTOM("query_mode_custom_time")
    }

}