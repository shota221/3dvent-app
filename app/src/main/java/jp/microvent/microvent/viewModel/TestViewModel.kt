package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.TestRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class TestViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    private val testRepository = TestRepository.instance
    private val repository = MicroventRepository.instance

    //画面遷移イベントの設定
    val onTransit = MutableLiveData<Event<String>>()

    fun onClickTestButton() {

        viewModelScope.launch {
            try{
                val idfv = "sample"
                val appkeyFetchForm: AppkeyFetchForm = AppkeyFetchForm(idfv)
                val apiToken = "secret"
                val response = repository.createAppkey(appkeyFetchForm,apiToken)
                if(response.isSuccessful){
                    onTransit.value = Event("onTransit")
                }
            }catch (e:Exception){
                Log.e("getJojo:Failed", e.stackTrace.toString())
            }
        }
    }

}