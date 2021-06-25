package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.UpdateUserForm
import jp.microvent.microvent.service.model.User
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class UserDataUpdateViewModel(
    private val myApplication: Application,
    val user: User
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val user: User
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserDataUpdateViewModel(application, user) as T
        }
    }

    val transitionToUserDataDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val organizationName: MutableLiveData<String> = MutableLiveData(user.organizationName)

    val userName: MutableLiveData<String> = MutableLiveData(user.userName)

    val email: MutableLiveData<String> = MutableLiveData(user.email)


    fun onClickSaveUserDataButton() {
        viewModelScope.launch {
            try {
                val updateUserForm = UpdateUserForm(userName.value,email.value)
                repository.updateUser(updateUserForm, appkey, userToken)
                    .let {
                        if (it.isSuccessful) {
                            showToastUpdated()
                            transitionToUserDataDetail.value =
                                Event("transitionToUserDataDetail")
                        } else {
                            errorHandling(it)
                        }
                    }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
        }
    }

}