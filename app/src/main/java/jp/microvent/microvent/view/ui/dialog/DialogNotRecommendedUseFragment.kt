package jp.microvent.microvent.view.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import jp.microvent.microvent.R
import jp.microvent.microvent.view.ui.VentilatorSettingFragment
import java.lang.ClassCastException

class DialogNotRecommendedUseFragment() : DialogFragment() {


    interface DialogNotRecommendedUseListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    lateinit var listener: DialogNotRecommendedUseListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val targetFragment = VentilatorSettingFragment()
        try {
            listener = targetFragment as DialogNotRecommendedUseListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement DialogNotRecommendedUseListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.not_recommended_use_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogPositiveClick(this)
            }

        return builder.create()
    }
}