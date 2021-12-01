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

class ChatViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val roomUri: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try{
                repository.fetchRoomUri(appkey).let { res ->
                    if (res.isSuccessful) {
                        roomUri.value = res.body()?.result?.uri
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                Log.e("ConnectionError", e.stackTraceToString())
                showToast.value = Event(context.getString(R.string.network_error))
            } finally {
                setProgressBar.value = Event(false)
            }
        }
    }
}