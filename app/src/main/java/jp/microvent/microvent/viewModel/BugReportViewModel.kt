package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreateBugReportForm
import jp.microvent.microvent.service.model.DialogDescription
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import kotlin.Exception


class BugReportViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val bugName: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val requestImprovement: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onClickSendBugReportButton() {
        viewModelScope.launch {
            try {
                val createBugReportForm = CreateBugReportForm(ventilatorId, bugName.value, requestImprovement.value)
                if (loggedIn()) {
                    repository.createBugReport(createBugReportForm, appkey, userToken)
                } else {
                    repository.createBugReportNoAuth(createBugReportForm, appkey)
                }.let {
                    if(it.isSuccessful) {
                        val message = context.getString(R.string.sent)
                        val dialogNotification = DialogDescription(null, message)
                        showDialogNotification.value = Event(dialogNotification)
                    } else {
                        errorHandling(it)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
                Log.e("test",e.stackTraceToString())
            }
        }
    }

    fun onClickDialogPositive() {
        bugName.value = ""
        requestImprovement.value = ""
    }
}