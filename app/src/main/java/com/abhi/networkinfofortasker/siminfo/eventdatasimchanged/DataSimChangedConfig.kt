package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigNoInput

class DataSimChangedConfig : Activity(), TaskerPluginConfigNoInput {
    override val context: Context
        get() = applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataSimChangedHelper(this).finishForTasker()
    }
}