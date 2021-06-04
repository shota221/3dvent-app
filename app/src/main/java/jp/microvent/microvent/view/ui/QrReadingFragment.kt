package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.mlkit.vision.barcode.Barcode
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentQrReadingBinding
import jp.microvent.microvent.view.permission.CameraPermission
import jp.microvent.microvent.viewModel.QrReadingViewModel
import jp.microvent.microvent.viewModel.TestViewModel
import jp.microvent.microvent.viewModel.util.CodeScanner
import jp.microvent.microvent.viewModel.util.EventObserver

class QrReadingFragment : Fragment() {

    private val qrReadingViewModel by viewModels<QrReadingViewModel>()

    private lateinit var binding: FragmentQrReadingBinding

    private lateinit var codeScanner: CodeScanner
    private val launcher = registerForActivityResult(
        CameraPermission.RequestContract(), ::onPermissionResult
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_reading, container, false)

        val viewModel = qrReadingViewModel

        binding.apply {
            qrReadingViewModel = viewModel
//            TODO("呼吸器読み取り時点で呼吸器と組織が紐付いている場合はその組織名を表示する")
            lifecycleOwner = viewLifecycleOwner
        }

        codeScanner = CodeScanner(requireActivity(), binding.previewView, ::onDetectCode)
        if (CameraPermission.hasPermission(requireActivity())) {
            start()
        } else {
            launcher.launch(Unit)
        }

        qrReadingViewModel.gs1Code.observe(
            viewLifecycleOwner, Observer {
                val action = QrReadingFragmentDirections.actionQrReadingToAuth(it)
                findNavController().navigate(action)
            }
        )


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun onPermissionResult(granted: Boolean) {
        if (granted) {
            start()
        } else {
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        }
    }

    private fun start() {
        codeScanner.start()
    }

    private fun onDetectCode(codes: List<Barcode>) {
        codes.forEach {
            val rawGs1Code = it.rawValue ?: return@forEach
            //gs1コードである宣言]C1も含まれる可能性があるため除去
            var gs1Code = rawGs1Code.replace("]C1","")
            //英数字以外の文字を消去
            gs1Code = gs1Code.replace("[^0-9a-zA-Z]".toRegex(),"")
            //医療用バーコードとして不適切な桁数(20文字以下)と判断できれば返却(参考：https://www.rolan.co.jp/shouhin/s_sakurabarcode5_medical1.html)
            if(gs1Code.length <= 20) return@forEach
            qrReadingViewModel.gs1Code.postValue(gs1Code)
        }
    }

}