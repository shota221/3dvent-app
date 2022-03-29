package jp.microvent.microvent.viewModel.util

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * CodeAnalyzerをProcessCameraProviderにbindする用
 */
class CodeScanner(
    private val activity: ComponentActivity,
    private val previewView: PreviewView,
    callback: (List<Barcode>) -> Unit
) {
    private val workerExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val scanner: BarcodeScanner = BarcodeScanning.getClient()
    private val analyzer: CodeAnalyzer = CodeAnalyzer(scanner, callback)
    private var camera: Camera? = null
    val torchState: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        activity.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    workerExecutor.shutdown()
                    scanner.close()
                }
            }
        )
    }

    fun start() {
        val future = ProcessCameraProvider.getInstance(activity)
        future.addListener({
            setUp(future.get())
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun setUp(provider: ProcessCameraProvider) {
        val preview = Preview.Builder()
            .build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        analysis.setAnalyzer(workerExecutor, analyzer)

        try {
            provider.unbindAll()
            provider.bindToLifecycle(
                activity, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis
            ).let {
                it.cameraInfo.torchState.observe(activity) {
                    torchState.postValue(it == TorchState.ON)
                }
                camera = it
            }
        } catch (e: Exception) {
            Log.e("CameraView:Failed", e.stackTraceToString())
        }
    }

    fun toggleTorch() {
        camera?.let {
            val next = !(torchState.value ?: false)
            it.cameraControl.enableTorch(next)
        }
    }
}