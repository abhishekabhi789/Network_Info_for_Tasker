package com.abhi.networkinfofortasker.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Toast
import com.abhi.networkinfofortasker.R

object Dialogue {
    /**Shows a single choice list*/
    fun Activity.showSingleChoiceDialog(
        title: String,
        variables: List<String>,
        callback: (String?) -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setIcon(R.mipmap.ic_launcher)
            setTitle(title)
            val arrayAdapter = ArrayAdapter<String>(
                this@showSingleChoiceDialog,
                com.google.android.material.R.layout.select_dialog_singlechoice_material
            ).apply {
                addAll(variables)
            }
            setAdapter(arrayAdapter) { _, which -> callback(arrayAdapter.getItem(which)) }
            setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

/*    *//**Shows a multiple choice list*//*
    fun showMultiChoiceDialog(
        context: Context,
        title: String,
        allItems: Array<String>,
        checkedItems: BooleanArray?,
        onSelected: (BooleanArray) -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setIcon(R.mipmap.ic_launcher)

        builder.setMultiChoiceItems(allItems, checkedItems) { _, which, isChecked ->
            checkedItems?.set(which, isChecked)
        }

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            onSelected(checkedItems ?: BooleanArray(allItems.size) { false })
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.setNeutralButton(R.string.clear_all) { _: DialogInterface?, _: Int ->
            onSelected(BooleanArray(allItems.size) { false })
        }
        builder.setCancelable(false)
        builder.create().show()
    }*/

    /**show the text as toast*/
    fun String.toToast(context: Context) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, this, Toast.LENGTH_LONG).show()
        }
    }
}