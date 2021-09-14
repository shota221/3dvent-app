package jp.microvent.microvent.view.adapter

import android.R
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class SpinnerBinder(val context:Context) {
    fun bind(spinner: Spinner, arrayList: List<String>, onItemSelected: (Int)->Unit, offset: Int?) {
        val adapter = ArrayAdapter<String>(
            context,
            R.layout.simple_spinner_item,
            arrayList
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        spinner.apply {
            setAdapter(adapter)

            setSelection(offset ?: 0)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onItemSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
    }
}