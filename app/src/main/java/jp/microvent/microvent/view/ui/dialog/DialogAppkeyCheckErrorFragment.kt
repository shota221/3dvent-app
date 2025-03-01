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
import java.lang.ClassCastException

class DialogAppkeyCheckErrorFragment() : DialogFragment(){


    interface DialogAppkeyCheckErrorListener {
        fun onDialogPositiveClick(dialog:DialogFragment)
    }

    lateinit var listener:DialogAppkeyCheckErrorListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            listener = context as DialogAppkeyCheckErrorListener
        }catch (e: ClassCastException){
            throw ClassCastException("${context.toString()} must implement DialogAppkeyCheckErrorListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.connection_error))
            .setMessage(getString(R.string.connection_error_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogPositiveClick(this)
            }

        return builder.create()
    }
}