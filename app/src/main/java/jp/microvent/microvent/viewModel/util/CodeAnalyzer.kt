package jp.microvent.microvent.viewModel.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage

class CodeAnalyzer(
    private val scanner: BarcodeScanner,
    private val callback: (List<Barcode>) -> Unit
): ImageAnalysis.Analyzer {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image
        if(image != null) {
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            scanner.process(inputImage).addOnSuccessListener{callback(it)}.addOnCompleteListener{imageProxy.close()}
        } else {
            imageProxy.close()
        }
    }
}