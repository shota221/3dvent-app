package jp.microvent.microvent.view.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.DialogNotification
import jp.microvent.microvent.view.ui.BaseFragment

class DialogNotificationFragment(private val targetFragment: BaseFragment, private val dialogNotification: DialogNotification) : DialogFragment(){

    interface  DialogNotificationListener {
        fun onDialogPositiveClick(dialog:DialogFragment)
    }

    lateinit var listener: DialogNotificationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = targetFragment as DialogNotificationListener
        }catch (e: ClassCastException){
            throw java.lang.ClassCastException("${context.toString()} must implement DialogConnectionErrorListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(dialogNotification.title)
            .setMessage(dialogNotification.message)
            .setPositiveButton(getString(R.string.ok)){ dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogPositiveClick(this)
            }

        return builder.create()
    }
}