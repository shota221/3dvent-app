package jp.microvent.microvent.viewModel

import android.app.Application
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.User
import jp.microvent.microvent.service.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    private val repository = UserRepository.instance
    var userLiveData: MutableLiveData<User> = MutableLiveData()

    init {
        loadData()
    }

    private fun loadData(){
        userLiveData.postValue(User())
    }

    class Factory(private val application:Application):ViewModelProvider.NewInstanceFactory(){
        override fun <T:ViewModel> create(modelClass: Class<T>): T{
            return AuthViewModel(application) as T
        }
    }

}