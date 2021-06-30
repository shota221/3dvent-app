package jp.microvent.microvent.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mlkit.vision.barcode.Barcode
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.DrawerOperationFlowBinding
import jp.microvent.microvent.databinding.FragmentQrReadingBinding
import jp.microvent.microvent.databinding.MeasurementDataListItemBinding
import jp.microvent.microvent.view.permission.AccessLocationPermission
import jp.microvent.microvent.view.permission.CameraPermission
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.QrReadingViewModel
import jp.microvent.microvent.viewModel.TestViewModel
import jp.microvent.microvent.viewModel.util.CodeScanner
import jp.microvent.microvent.viewModel.util.EventObserver

class QrReadingFragment : DrawableFragment() {

    private val qrReadingViewModel by viewModels<QrReadingViewModel>()

    private lateinit var binding: FragmentQrReadingBinding

    private lateinit var codeScanner: CodeScanner

    private val cameraPermissionLauncher = registerForActivityResult(
        CameraPermission.RequestContract(), ::onCameraPermissionResult
    )
    private val accessLocationPermissionLauncher = registerForActivityResult(
        AccessLocationPermission.RequestContract(), ::onAccessLocationPermissionResult
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
            codeScanner.start()
        } else {
            cameraPermissionLauncher.launch(Unit)
        }

        if (AccessLocationPermission.hasPermission(requireActivity())) {
            qrReadingViewModel.setLocation()
        } else {
            accessLocationPermissionLauncher.launch(Unit)
        }

        //画面制御用オブザーバーセット
        qrReadingViewModel.apply {
            transitionToPatientSetting.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_qr_reading_to_patient_setting)
                }
            )
            transitionToVentilatorSetting.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_qr_reading_to_ventilator_setting)
                }
            )

            transitionToAuth.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_to_auth)
                }
            )

            /**
             * ヘルプボタン監視
             */
            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = QrReadingFragmentDirections.actionToHelp(getString(R.string.qr_reading_manual_path))
                    findNavController().navigate(action)
                }
            )

            /**
             *　ドロワーボタン監視
             */
            showFlowDrawer.observe(
                viewLifecycleOwner, EventObserver{
                    showFlowDrawer(R.id.point_flow_qr_reading)
                }
            )

            /**
             * 通信エラーダイアログの表示
             */
            showDialogConnectionError.observe(
                viewLifecycleOwner,
                EventObserver {
                    val dialog = DialogConnectionErrorFragment()
                    dialog.show(requireActivity().supportFragmentManager, it)
                }
            )

            /**
             * トースト表示
             */
            showToast.observe(
                viewLifecycleOwner,
                EventObserver {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                }
            )

            /**
             * プログレスバー制御
             */
            setProgressBar.observe(
                viewLifecycleOwner,
                EventObserver {
                    progressBar.visibility = if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            )
        }

        return binding.root
    }

    private fun onCameraPermissionResult(granted: Boolean) {
        if (granted) {
            codeScanner.start()
        } else {
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        }
    }

    private fun onDetectCode(codes: List<Barcode>) {
        codes.forEach {
            val rawGs1Code = it.rawValue ?: return@forEach
            //gs1コードである宣言]C1も含まれる可能性があるため除去
            var gs1Code = rawGs1Code.replace("]C1", "")
            //英数字以外の文字を消去
            gs1Code = gs1Code.replace("[^0-9a-zA-Z]".toRegex(), "")
            //医療用バーコードとして不適切な桁数(20文字以下)と判断できれば返却(参考：https://www.rolan.co.jp/shouhin/s_sakurabarcode5_medical1.html)
            if (gs1Code.length <= 20) return@forEach
            qrReadingViewModel.gs1Code.value = gs1Code
        }
    }

    private fun onAccessLocationPermissionResult(granted: Boolean) {
        if (granted) {
            qrReadingViewModel.setLocation()
        }
    }
}