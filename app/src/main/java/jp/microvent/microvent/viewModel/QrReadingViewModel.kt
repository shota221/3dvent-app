package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.model.CreateVentilatorForm
import jp.microvent.microvent.service.model.UpdateVentilatorForm
import jp.microvent.microvent.view.permission.CameraPermission
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class QrReadingViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val gs1Code: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val gs1CodeForTest: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    fun test(){
        gs1Code.postValue(gs1CodeForTest.value)
    }
}