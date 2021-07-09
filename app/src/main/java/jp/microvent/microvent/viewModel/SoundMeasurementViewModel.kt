package jp.microvent.microvent.viewModel

import android.app.Application
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.OnRecordPositionUpdateListener
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.viewModel.util.Event
import jp.microvent.microvent.viewModel.util.WaveBuilder
import kotlinx.coroutines.launch
import java.io.File


class SoundMeasurementViewModel(
    myApplication: Application,
    ventilatorValue: VentilatorValue
) : IeMeasurementViewModel(myApplication, ventilatorValue) {

    class Factory(
        private val application: Application, private val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SoundMeasurementViewModel(application, ventilatorValue) as T
        }
    }

    val transitionToManualMeasurement: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val filePath = context.filesDir.path + "/" + context.getString(R.string.temp_wav_name)

    val recordStatus: MutableLiveData<RecordStatus> = MutableLiveData(RecordStatus.WAITING)

    val recordStatusStr: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val recordButtonStr: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    init {
        val recordStatusObserver = Observer<RecordStatus> {
            recordStatusStr.value = when (it) {
                RecordStatus.WAITING -> context.getString(R.string.waiting)
                RecordStatus.RECORDING -> context.getString(R.string.recording)
            }

            recordButtonStr.value = when (it) {
                RecordStatus.WAITING -> context.getString(R.string.rec)
                RecordStatus.RECORDING -> context.getString(R.string.stop)
            }
        }

        recordStatusStr.addSource(recordStatus, recordStatusObserver)
        recordButtonStr.addSource(recordStatus, recordStatusObserver)
    }

    override fun onClickCalculateAverageButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val fileData = File(filePath).readBytes()
                val encodedData = Base64.encodeToString(fileData, Base64.DEFAULT)
                val ieSoundFetchFormSoundElm = IeSoundFetchFormSoundElm(fileData = encodedData)
                val ieSoundFetchForm = IeSoundFetchForm(ieSoundFetchFormSoundElm)
                val calcIeSound = repository.calcIeSound(ieSoundFetchForm, appkey)
                if (calcIeSound.isSuccessful) {
                    calcIeSound.body()?.result?.let {
                        averageInhalationTime.postValue(it.iAvg)
                        setUnit(
                            averageInhalationTimeWithUnit,
                            it.iAvg,
                            context.getString(R.string.i_avg_pref_key)
                        )
                        averageExhalationTime.postValue(it.eAvg)
                        setUnit(
                            averageExhalationTimeWithUnit,
                            it.eAvg,
                            context.getString(R.string.e_avg_pref_key)
                        )
                        rr.postValue(it.rr)
                        ieRatio.postValue(it.ieRatio)
                    }
                } else {
                    showToast.value = Event(context.getString(R.string.bad_sound))
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
            setProgressBar.value = Event(false)
        }
    }

    fun onClickManualMeasurementButton() {
        transitionToManualMeasurement.value = Event("transitionToManualMeasurement")
    }

    fun onClickRecordButton() {
        try {
            when (recordStatus.value) {
                RecordStatus.WAITING -> {
                    startAudioRecord()
                }

                RecordStatus.RECORDING -> {
                    stopAudioRecord()
                }
            }
            recordStatus.value = recordStatus.value?.next() ?: RecordStatus.WAITING
        } catch (e: Exception) {
            Log.e("calculate:Failed", e.stackTrace.toString())
        }
    }


//MediaRecorderを用いるパターンだとうまくwavとして録音できなかったため、AudioRecordを使用

    private lateinit var shortData: ShortArray

    private val samplingRate = 44100 //オーディオレコード用サンプリング周波数

    private val bufSize by lazy {
        AudioRecord.getMinBufferSize(
            samplingRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
    }//オーディオレコード用バッファのサイズ

    private val audioRecord: AudioRecord by lazy {
        AudioRecord(
            MediaRecorder.AudioSource.MIC,
            samplingRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufSize
        )
    }

    private val wav: WaveBuilder = WaveBuilder()

    //AudioRecordの初期化
    private fun initAudioRecord() {
        wav.createFile(filePath)

        shortData = ShortArray(bufSize / 2)

        // コールバックを指定
        audioRecord.setRecordPositionUpdateListener(object : OnRecordPositionUpdateListener {
            // フレームごとの処理
            override fun onPeriodicNotification(recorder: AudioRecord) {
                audioRecord.read(shortData, 0, bufSize / 2) // 読み込む
                wav.addBigEndianData(shortData) // ファイルに書き出す
            }

            override fun onMarkerReached(recorder: AudioRecord) {
            }
        })
        // コールバックが呼ばれる間隔を指定
        audioRecord.positionNotificationPeriod = bufSize / 2
    }

    private fun startAudioRecord() {
        initAudioRecord()
        audioRecord.startRecording()
        audioRecord.read(shortData, 0, bufSize / 2)
    }

    //オーディオレコードを停止する
    private fun stopAudioRecord() {
        audioRecord.stop()
    }
}