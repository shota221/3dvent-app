package jp.microvent.microvent.view.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.DialogDescription
import jp.microvent.microvent.view.ui.BaseFragment

class DialogConfirmationFragment(private val targetFragment: BaseFragment, private val dialogDescription: DialogDescription) : DialogFragment(){

    interface  DialogConfirmationListener {
        fun onDialogPositiveClick(dialog:DialogFragment)
        fun onDialogNegativeClick(dialog:DialogFragment)
    }

    lateinit var listener: DialogConfirmationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = targetFragment as DialogConfirmationListener
        }catch (e: ClassCastException){
            throw java.lang.ClassCastException("${context.toString()} must implement DialogConnectionErrorListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(dialogDescription.title)
            .setMessage(dialogDescription.message)
            .setPositiveButton(getString(R.string.ok)){ dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogPositiveClick(this)
            }.setNegativeButton(getString(R.string.cancel)){ dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogNegativeClick(this)
            }

        return builder.create()
    }
}