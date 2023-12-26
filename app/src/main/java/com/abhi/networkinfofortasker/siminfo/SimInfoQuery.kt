package com.abhi.networkinfofortasker.siminfo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import com.abhi.networkinfofortasker.utils.PermissionHelper
import java.lang.reflect.InvocationTargetException


class SimInfoQuery {

    private fun getSubscriptionManager(context: Context): SubscriptionManager {
        return context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    }

    private fun getSimNameForIndex(index: Int): String {
        for (slot in SimSlots.entries) {
            if (slot.index == index) {
                return slot.label!!
            }
        }
        return ""
    }

    @SuppressLint("MissingPermission")
    fun getSimDetails(context: Context, index: Int): SimInfo? {
        val subscriptionManager = getSubscriptionManager(context)
        val info = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(index)
        return if (info != null) SimInfo(
            info.displayName.toString(),
            info.number,
            info.simSlotIndex +1, //tasker needs index starting at zero
            getSimNameForIndex(index)
        ) else null
    }

    @SuppressLint("MissingPermission")
    fun getDataSimOperator(context: Context): SimInfo {
        val subscriptionManager = getSubscriptionManager(context)
        val subId = getDefaultDataSubscriptionId(subscriptionManager)
        val info = subscriptionManager.getActiveSubscriptionInfo(subId)
        return SimInfo(
            info.displayName.toString(),
            info.number,
            info.simSlotIndex +1,
            getSimNameForIndex(info.simSlotIndex)
        )
    }

    private fun getDefaultDataSubscriptionId(subscriptionManager: SubscriptionManager): Int {
        //source: https://stackoverflow.com/a/46760070
        if (Build.VERSION.SDK_INT >= 24) {
            val nDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId()
            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
                return nDataSubscriptionId
            }
        }
        try {
            val subscriptionClass = Class.forName(subscriptionManager.javaClass.name)
            try {
                val getDefaultDataSubscriptionId =
                    subscriptionClass.getMethod("getDefaultDataSubId")
                try {
                    return getDefaultDataSubscriptionId.invoke(subscriptionManager) as Int
                } catch (e1: IllegalAccessException) {
                    e1.printStackTrace()
                } catch (e2: InvocationTargetException) {
                    e2.printStackTrace()
                }
            } catch (e3: NoSuchMethodException) {
                e3.printStackTrace()
            }
        } catch (e4: ClassNotFoundException) {
            e4.printStackTrace()
        }
        return -1
    }

    fun getMissingPermissions(context: Context): List<PermissionHelper.Permission> {
        val requiredPermissions = listOf(PermissionHelper.Permission.PHONE_STATE)
        return PermissionHelper.getMissingPermissions(context, requiredPermissions)
    }
}