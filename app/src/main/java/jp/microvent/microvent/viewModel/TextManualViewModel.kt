package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.VentilatorValue
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class TextManualViewModel(
    private val myApplication: Application,
) : BaseViewModel(myApplication) {
}