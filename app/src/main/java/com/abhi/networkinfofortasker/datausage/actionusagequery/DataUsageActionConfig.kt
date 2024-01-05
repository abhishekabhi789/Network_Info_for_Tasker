package com.abhi.networkinfofortasker.datausage.actionusagequery

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.abhi.networkinfofortasker.NetworkType
import com.abhi.networkinfofortasker.databinding.DataUsageActionInputBinding
import com.abhi.networkinfofortasker.datausage.DataUsageQuery
import com.abhi.networkinfofortasker.utils.Tasker.chooseVariable
import com.abhi.networkinfofortasker.utils.Tasker.saveConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class DataUsageActionConfig : Activity(), TaskerPluginConfig<DataUsageActionInput> {
    private lateinit var binding: DataUsageActionInputBinding
    private val taskerHelper by lazy { DataUsageActionHelper(this) }
    override val context: Context get() = applicationContext
    override val inputForTasker: TaskerInput<DataUsageActionInput>
        get() = TaskerInput(
            DataUsageActionInput(
                binding.root.resources.getResourceEntryName(binding.queryMode.checkedRadioButtonId),
                binding.root.resources.getResourceEntryName(binding.networkType.checkedRadioButtonId),
                getChosenSimId(),
                binding.startTimeInput.text.toString(),
                binding.endTimeInput.text.toString(),
                binding.appListInput.text.toString().trim(),
                binding.uidAll.isChecked,
                binding.uidRemoved.isChecked,
                binding.uidTethering.isChecked
            )
        )

    private fun getChosenSimId(): String? {
        return if (binding.networkType.checkedRadioButtonId == binding.networkTypeMobile.id) {
            binding.root.resources.getResourceEntryName(binding.simSlots.checkedRadioButtonId)
        } else null
    }

    override fun assignFromInput(input: TaskerInput<DataUsageActionInput>) {
        input.regular.run {
            if (queryMode.isNotEmpty()) {
                binding.queryMode.check(
                    binding.root.resources.getIdentifier(
                        queryMode, "id", this@DataUsageActionConfig.packageName
                    )
                )
            }
            if (networkType.isNotEmpty()) {
                binding.networkType.check(
                    binding.root.resources.getIdentifier(
                        networkType, "id", this@DataUsageActionConfig.packageName
                    )
                )
            }
            if (chosenSim != null) {
                binding.simSlots.check(
                    binding.root.resources.getIdentifier(
                        chosenSim, "id", this@DataUsageActionConfig.packageName
                    )
                )
            } else if (networkType == NetworkType.MOBILE.id) binding.simSlots.check(
                binding.simSlotDefaultData.id
            )
            binding.startTimeInput.setText(startTime)
            binding.endTimeInput.setText(endTime)
            binding.appListInput.setText(appPackages)
            binding.uidAll.isChecked = uidAll
            binding.uidRemoved.isChecked = uidRemoved
            binding.uidTethering.isChecked = uidTethering
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var chosenSlot: Int? = null
        super.onCreate(savedInstanceState)
        binding = DataUsageActionInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskerHelper.onCreate()
        binding.customQueryInput.visibility =
            if (binding.queryMode.checkedRadioButtonId == binding.queryModeCustomTime.id) View.VISIBLE else View.GONE
        binding.simSlotSelection.visibility =
            if (binding.networkType.checkedRadioButtonId == binding.networkTypeMobile.id) View.VISIBLE else View.GONE
        binding.chooseStartTimeButton.setOnClickListener {
            chooseVariable(this, binding.startTimeInput.id, taskerHelper)
        }
        binding.chooseEndTimeButton.setOnClickListener {
            chooseVariable(this, binding.endTimeInput.id, taskerHelper)
        }
        binding.queryMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.queryModeCustomTime.id -> binding.customQueryInput.visibility = View.VISIBLE
                else -> binding.customQueryInput.visibility = View.GONE
            }
        }
        binding.networkType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.networkTypeMobile.id -> {
                    binding.simSlotSelection.visibility = View.VISIBLE
                    binding.simSlots.check(chosenSlot ?: binding.simSlotDefaultData.id)
                }

                else -> {
                    chosenSlot = binding.simSlots.checkedRadioButtonId
                    binding.simSlotSelection.visibility = View.GONE
                }
            }
        }
        binding.simSlots.setOnCheckedChangeListener { _, id ->
            chosenSlot = id
        }
        binding.appListVariableButton.setOnClickListener {
            chooseVariable(this, binding.appListInput.id, taskerHelper)
        }
        binding.saveConfigButton.setOnClickListener {
            saveConfig(this, taskerHelper, DataUsageQuery.requiredPermissions)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            saveConfig(this, taskerHelper, DataUsageQuery.requiredPermissions)
        }
        return super.onKeyDown(keyCode, event)
    }
}