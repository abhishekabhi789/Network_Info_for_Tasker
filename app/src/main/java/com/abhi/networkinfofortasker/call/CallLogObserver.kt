package com.abhi.networkinfofortasker.call

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.CallLog
import android.util.Log
import com.abhi.networkinfofortasker.call.eventcalllogentry.CallLogEntryEventConfig
import com.abhi.networkinfofortasker.call.eventcalllogentry.CallLogEntryEventOutput
import com.abhi.networkinfofortasker.siminfo.SimInfoQuery
import com.abhi.networkinfofortasker.utils.PermissionHelper
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery


class CallLogObserver(private val mContext: Context, handler: Handler?) :
    ContentObserver(handler) {
    private val TAG = javaClass.simpleName
    private var lastUpdate: MutableMap<String, Any?>? = null
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (uri != null && uri == CallLog.Calls.CONTENT_URI) {
            val callDetails = getLastCallDetails(mContext)
            if (callDetails != null) {
                if (callDetails != lastUpdate) {
                    Log.d(TAG, "onChange: Content changed!")
                    Log.d(TAG, "onChange: update- $callDetails")
                    lastUpdate = callDetails
                    val update = CallLogEntryEventOutput(
                        callDetails["phoneNumber"] as String,
                        callDetails["callerName"] as String?,
                        callDetails["callType"] as String,
                        callDetails["callTime"] as Long,
                        callDetails["simIndex"] as Int?
                    )
                    CallLogEntryEventConfig::class.java.requestQuery(mContext, update)
                }
            } else Log.d(TAG, "onChange: no change found")
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun getLastCallDetails(context: Context): MutableMap<String, Any?>? {
        val contentResolver = this.mContext.contentResolver
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )
        return if (cursor?.moveToFirst() == true) {
            val valuesMap = mutableMapOf<String, Any?>()
            valuesMap["phoneNumber"] = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            valuesMap["callerName"] =
                cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
            val callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
            valuesMap["callType"] = getCallType(callType)
            valuesMap["callTime"] = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
            val subscriptionId =
                cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID))
            val simIndex = SimInfoQuery().getSimIndexFromSubscriptionId(context, subscriptionId)
            valuesMap["simIndex"] = if (simIndex >= 0) simIndex + 1 else null
            return valuesMap
        } else null
    }

    private fun getCallType(type: Int): String {
        return when (type) {
            CallLog.Calls.INCOMING_TYPE -> "incoming"
            CallLog.Calls.MISSED_TYPE -> "missed"
            CallLog.Calls.OUTGOING_TYPE -> "outgoing"
            CallLog.Calls.BLOCKED_TYPE -> "blocked"
            CallLog.Calls.REJECTED_TYPE -> "rejected"
            CallLog.Calls.ANSWERED_EXTERNALLY_TYPE -> "answered_externally"
            else -> "unknown"
        }
    }

    companion object {
        val neededPermission = listOf(
            PermissionHelper.Permission.READ_CALL_LOG,
            PermissionHelper.Permission.READ_PHONE_STATE
        )
    }
}