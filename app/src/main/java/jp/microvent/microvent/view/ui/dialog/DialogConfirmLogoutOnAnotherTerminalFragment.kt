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
import jp.microvent.microvent.view.ui.AuthFragment
import java.lang.ClassCastException

class DialogConfirmLogoutOnAnotherTerminalFragment() : DialogFragment() {


    interface DialogconfirmLogoutOnAnotherTerminalListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    lateinit var listener: DialogconfirmLogoutOnAnotherTerminalListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val targetFragment = AuthFragment()
        try {
            listener = targetFragment as DialogconfirmLogoutOnAnotherTerminalListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement DialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.using_another_terminal))
            .setMessage(getString(R.string.using_another_terminal_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogPositiveClick(this)
            }

        return builder.create()
    }
}