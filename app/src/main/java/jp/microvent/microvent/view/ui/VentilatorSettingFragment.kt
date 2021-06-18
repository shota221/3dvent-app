package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorSettingBinding
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver


class VentilatorSettingFragment : Fragment() {

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

        ventilatorSettingViewModel.transitionToManualMeasurement.observe(
            viewLifecycleOwner, EventObserver {
                val ventilatorValue = ventilatorSettingViewModel.ventilatorValue
                val action = VentilatorSettingFragmentDirections.actionVentilatorSettingToManualMeasurement(ventilatorValue)
                findNavController().navigate(action)
            }
        )

        ventilatorSettingViewModel.transitionToSoundMeasurement.observe(
            viewLifecycleOwner, EventObserver {
                val ventilatorValue = ventilatorSettingViewModel.ventilatorValue
                val action = VentilatorSettingFragmentDirections.actionVentilatorSettingToSoundMeasurement(ventilatorValue)
                findNavController().navigate(action)
            }
        )

        return binding.root
    }
}