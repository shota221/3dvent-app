package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.TestRepository
import jp.microvent.microvent.service.repository.UserRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    private val repository = MicroventRepository.instance
    private val testRepository = TestRepository.instance

    public fun checkUserToken(accountName: String?): Boolean? {
        return true
    }

    public fun getUserToken(accountName: String?,password: String?): String {
        return "test"
    }

    val onTransit = MutableLiveData<Event<String>>()

    fun onClickLoginButton() {

        viewModelScope.launch{
            try{
                val accountName = "テスト更新太郎@example"
                val appkey = "49fac174db39a29b65803f6981dc4db13ebe8cce"
                val response = repository.checkUserToken(accountName,appkey)
//                val response = testRepository.getJojo()
                if(response.isSuccessful){
                    onTransit.value = Event("onTransit")
                }
            }catch (e:Exception){
                Log.e("checkUserToken:Failed", e.stackTraceToString())
            }
        }
    }
}