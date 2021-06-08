package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentManualMeasurementBinding
import jp.microvent.microvent.viewModel.ManualMeasurementViewModel
import jp.microvent.microvent.viewModel.VentilatorResultViewModel

class ManualMeasurementFragment : Fragment() {

    private val args: ManualMeasurementFragmentArgs by navArgs()

    private val manualMeasurementViewModel by lazy {
        ViewModelProvider(this, ManualMeasurementViewModel.Factory(
            requireActivity().application, args.ventilatorValue
        )).get(ManualMeasurementViewModel::class.java)
    }

    private lateinit var binding: FragmentManualMeasurementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manual_measurement, container, false)

        val viewModel = manualMeasurementViewModel

        binding.apply {
            manualMeasurementViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        manualMeasurementViewModel.transitionToVentilatorResult.observe(
            viewLifecycleOwner, Observer {
                val ventilatorValue = manualMeasurementViewModel.ventilatorValue
                val action = ManualMeasurementFragmentDirections.actionManualMeasurementToVentilatorResult(ventilatorValue)
                findNavController().navigate(action)
            }
        )

        return binding.root
    }
}