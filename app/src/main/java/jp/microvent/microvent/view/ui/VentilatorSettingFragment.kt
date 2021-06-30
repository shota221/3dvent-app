package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorSettingBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver


class VentilatorSettingFragment : DrawableFragment() {

    private val ventilatorSettingViewModel by viewModels<VentilatorSettingViewModel>()

    private lateinit var binding: FragmentVentilatorSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_ventilator_setting,container,false)

        val viewModel = ventilatorSettingViewModel

        binding.apply {
            ventilatorSettingViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        ventilatorSettingViewModel.apply {
            transitionToManualMeasurement.observe(
                viewLifecycleOwner, EventObserver {
                    val ventilatorValue = ventilatorSettingViewModel.ventilatorValue
                    val action =
                        VentilatorSettingFragmentDirections.actionVentilatorSettingToManualMeasurement(
                            ventilatorValue
                        )
                    findNavController().navigate(action)
                }
            )

            transitionToSoundMeasurement.observe(
                viewLifecycleOwner, EventObserver {
                    val ventilatorValue = ventilatorSettingViewModel.ventilatorValue
                    val action =
                        VentilatorSettingFragmentDirections.actionVentilatorSettingToSoundMeasurement(
                            ventilatorValue
                        )
                    findNavController().navigate(action)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = VentilatorSettingFragmentDirections.actionToHelp(getString(R.string.ventilator_setting_manual_path))
                    findNavController().navigate(action)
                }
            )

            showFlowDrawer.observe(
                viewLifecycleOwner, EventObserver{
                    showFlowDrawer(R.id.point_flow_ventilator_setting)
                }
            )

            transitionToAuth.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_to_auth)
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
}