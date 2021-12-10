package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.ConnectException

class ChatViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val roomUri: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try{
                repository.fetchRoomUri(sharedAccessToken.appkey).let { res ->
                    if (res.isSuccessful) {
                        roomUri.value = res.body()?.result?.uri
                    } else {
                        handleErrorResponse(res)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            } finally {
                setProgressBar.value = Event(false)
            }
        }
    }
}