package jp.microvent.microvent.view.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.DialogDescription
import jp.microvent.microvent.view.ui.BaseFragment

class DialogNotificationFragment(private val targetFragment: BaseFragment, private val dialogDescription: DialogDescription) : DialogFragment(){

    interface  DialogNotificationListener {
        fun onDialogClick(dialog:DialogFragment)
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

        builder.setTitle(dialogDescription.title)
            .setMessage(dialogDescription.message)
            .setPositiveButton(getString(R.string.ok)){ dialog, id ->
                println("dialog:$dialog which:$id")
                listener.onDialogClick(this)
            }

        return builder.create()
    }
}