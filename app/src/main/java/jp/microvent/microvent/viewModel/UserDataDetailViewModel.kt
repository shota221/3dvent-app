package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.User
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class UserDataDetailViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val user:MutableLiveData<User> by lazy {
        MutableLiveData()
    }

    val transitionToUserDataUpdate:MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickEditUserDataButton() {
        transitionToUserDataUpdate.value = Event("transitionToUserDataUpdate")
    }

    init {
        viewModelScope.launch {
            try {

                    repository.getUser(sharedAccessToken.appkey, sharedAccessToken.userToken).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            user.postValue(it)
                        }
                    } else {
                        handleErrorResponse(res)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            }
        }
    }
}