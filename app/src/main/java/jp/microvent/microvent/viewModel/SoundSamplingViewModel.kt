package jp.microvent.microvent.viewModel

import android.app.Application
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.OnRecordPositionUpdateListener
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.viewModel.util.Event
import jp.microvent.microvent.viewModel.util.WaveBuilder
import kotlinx.coroutines.launch
import java.io.File


class SoundSamplingViewModel(
    private val myApplication: Application,
) : BaseViewModel(myApplication) {
    lateinit var recorder: MediaRecorder
    lateinit var player: MediaPlayer

    val filePath = context.filesDir.path + "/" + context.getString(R.string.temp_wav_name)

    val recordStatus: MutableLiveData<RecordStatus> = MutableLiveData(RecordStatus.WAITING)

    val recordStatusStr: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val recordButtonStr: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val averageInhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageExhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }


    init {
        val recordStatusObserver = Observer<RecordStatus> {
            recordStatusStr.value = it.getStatusString(context)
            recordButtonStr.value = it.getButtonString(context)
        }

        recordStatusStr.addSource(recordStatus, recordStatusObserver)
        recordButtonStr.addSource(recordStatus, recordStatusObserver)
    }


    fun onClickSubmitButton() {
//        viewModelScope.launch {
//            try {
//                val fileData = File(filePath).readBytes()
//                val encodedData = Base64.encodeToString(fileData, Base64.DEFAULT)
//                Log.i("recordingtest", encodedData)
//                val ieSoundFetchFormSoundElm = IeSoundFetchFormSoundElm(file_data = encodedData)
//                val ieSoundFetchForm = IeSoundFetchForm(ieSoundFetchFormSoundElm)
//                val soundSampling = repository.soundSampling(ieSoundFetchForm, apiToken)
//                if (soundSampling.isSuccessful) {
//                    Log.i("recordingtest", soundSampling.body()?.result.toString())
//                } else {
//                    Log.i("recodingtest", soundSampling.errorBody().toString())
//                }
//            } catch (e: Exception) {
//                Log.e("calculate:Failed", e.stackTrace.toString())
//            }
//        }
        viewModelScope.launch {
            try {
                val fileData = File(filePath).readBytes()
                val encodedData = Base64.encodeToString(fileData, Base64.DEFAULT)
                Log.i("recordingtest", encodedData)
                val ieSoundFetchFormSoundElm = IeSoundFetchFormSoundElm(fileData = encodedData)
                val ieSoundFetchForm = IeSoundFetchForm(ieSoundFetchFormSoundElm)
                val calcIeSound = repository.calcIeSound(ieSoundFetchForm, sharedAccessToken.appkey)
                if (calcIeSound.isSuccessful) {
                    calcIeSound.body()?.result?.let{
                        averageInhalationTime.postValue(it.iAvg)
                        averageExhalationTime.postValue(it.eAvg)
                    }
                } else {
                    Log.i("calcIe",sharedAccessToken.appkey)
                    Log.i("calcIe", calcIeSound.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("calculate:Failed", e.stackTrace.toString())
            }
        }
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

    private val audioRecord:AudioRecord by lazy{
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

    //再生用
    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (e: Exception) {
                Log.e("test", "prepare() failed")
            }
        }
    }

    fun onClickPlayButton(){
        startPlaying()
    }

}