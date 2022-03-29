package jp.microvent.microvent.view.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData

class DialogDatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener{

    val dateString: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH);

        return DatePickerDialog(requireActivity(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //何故かmonthが0-originなので補正
        dateString.value = "$year-${month+1}-$dayOfMonth"
    }
}