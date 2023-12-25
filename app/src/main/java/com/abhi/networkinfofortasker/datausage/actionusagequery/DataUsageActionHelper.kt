package com.abhi.networkinfofortasker.datausage.actionusagequery

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper

class DataUsageActionHelper(config: TaskerPluginConfig<DataUsageActionInput>) :
    TaskerPluginConfigHelper<DataUsageActionInput, DataUsageActionOutput, DataUsageActionRunner>(
        config
    ) {
    override val inputClass: Class<DataUsageActionInput>
        get() = DataUsageActionInput::class.java
    override val outputClass: Class<DataUsageActionOutput>
        get() = DataUsageActionOutput::class.java
    override val runnerClass: Class<DataUsageActionRunner>
        get() = DataUsageActionRunner::class.java
}