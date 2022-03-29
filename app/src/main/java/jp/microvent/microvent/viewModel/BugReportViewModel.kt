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
import java.net.ConnectException
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
            setProgressBar.value = Event(true)
            try {
                val createBugReportForm = CreateBugReportForm(sharedCurrentVentilator.ventilatorId, bugName.value, requestImprovement.value)
                if (loggedIn()) {
                    repository.createBugReport(createBugReportForm, sharedAccessToken.appkey, sharedAccessToken.userToken)
                } else {
                    repository.createBugReportNoAuth(createBugReportForm, sharedAccessToken.appkey)
                }.let {
                    if(it.isSuccessful) {
                        val message = context.getString(R.string.sent)
                        val dialogNotification = DialogDescription(null, message)
                        showDialogNotification.value = Event(dialogNotification)
                    } else {
                        handleErrorResponse(it)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            } finally {
                setProgressBar.value = Event(false)
            }
        }
    }

    fun onClickDialogPositive() {
        bugName.value = ""
        requestImprovement.value = ""
    }
}