package jp.microvent.microvent.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.microvent.microvent.R
import java.io.IOException

private const val LOG_TAG = "SoundSampling"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

/**
 * A simple [Fragment] subclass.
 * Use the [SoundSamplingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoundSamplingFragment : Fragment() {
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private var tempWavFile: String = ""
    private var player: MediaPlayer? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var isRecMode: Boolean = true; //trueで録音受付（未録音状態）、falseで停止受付（録音中状態）
    private var isPlayMode: Boolean = true; //trueで再生受付（未再生状態）、falseで停止受付（再生中状態）

    /**
     * 認証許可
     * リクエストコードを受け取ってそれが200なら
     * 処理を続ける
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if(!permissionToRecordAccepted) {
            val transaction = fragmentManager?.beginTransaction()

            transaction?.remove(this@SoundSamplingFragment)

            transaction?.commit()
        }
    }

    /**
     * 録音・停止振り分け
     */
    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    /**
     * 再生・停止振り分け
     */
    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        tempWavFile = context?.cacheDir.toString() + "/tmp.wav"
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setOutputFile(tempWavFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sound_sampling, container, false)

        val etFileName = view.findViewById<EditText>(R.id.etFileName)

        val tvRecordingStatus = view.findViewById<TextView>(R.id.tvRecodingStatus)

        val btRec = view.findViewById<Button>(R.id.btRec)

        val btPlay = view.findViewById<Button>(R.id.btPlay)

        val btSend = view.findViewById<Button>(R.id.btSend)

        etFileName.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                fileName = etFileName.text.toString() + ".wav"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        val listener = SoundSamplingButton()

        btRec.setOnClickListener(listener)
        btPlay.setOnClickListener(listener)
        btSend.setOnClickListener(listener)


        return view
    }

    private inner class SoundSamplingButton : View.OnClickListener {
        override fun onClick(v: View?) {
            Log.i(LOG_TAG, "成功")
            Log.i(LOG_TAG, fileName)
            val tvRecodingStatus = activity?.findViewById<TextView>(R.id.tvRecodingStatus)

            if(v != null){
                when(v.id){
                    R.id.btRec -> {
                        val btRec = v.findViewById<Button>(R.id.btRec)
                        onRecord(isRecMode)
                        isRecMode = !isRecMode
                        if (tvRecodingStatus != null) {
                            tvRecodingStatus.text = if (isRecMode) getString(R.string.waiting) else getString(R.string.recording)
                        }
                        btRec.text = if (isRecMode) {
                            Log.i(LOG_TAG,"録音終了")
                            getString(R.string.rec) + tempWavFile
                        } else {
                            Log.i(LOG_TAG,"録音開始")
                            getString(R.string.stop) + tempWavFile
                        }

                    }

                    R.id.btPlay -> {
                        val btPlay = v.findViewById<Button>(R.id.btPlay)
                        onPlay(isPlayMode)
                        isPlayMode = !isPlayMode
                        btPlay.text = if (isPlayMode) {
                            Log.i(LOG_TAG,"再生停止")
                            getString(R.string.play)
                        } else {
                            Log.i(LOG_TAG,"再生開始")
                            getString(R.string.stop)
                        }
                    }

                    R.id.btSend -> {

                        Toast.makeText(activity, getString(R.string.transmission_complete), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SoundSamplingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SoundSamplingFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}

