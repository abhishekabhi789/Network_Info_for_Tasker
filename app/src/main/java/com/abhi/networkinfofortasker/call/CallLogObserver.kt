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
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (uri != null && uri == CallLog.Calls.CONTENT_URI) {
            val update = getLastCallDetails(mContext)
            CallLogEntryEventConfig::class.java.requestQuery(mContext, update)
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun getLastCallDetails(context: Context): CallLogEntryEventOutput? {
        val contentResolver = this.mContext.contentResolver
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )
        return if (cursor?.moveToFirst() == true) {
            val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
            val type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
            val timeStamp = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
            val subscriptionId =
                cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID))
            val simIndex: Int =
                SimInfoQuery().getSimIndexFromSubscriptionId(context, subscriptionId)
            val realSimIndex = if (simIndex >= 0) simIndex + 1 else null
            Log.d(
                TAG,
                "getLastCallDetails: subsciptionId: $subscriptionId | simIndex: $simIndex | realSimIndex: $realSimIndex"
            )
            CallLogEntryEventOutput(number, name, getCallType(type), timeStamp, realSimIndex)
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