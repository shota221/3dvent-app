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
import jp.microvent.microvent.databinding.FragmentVentilatorResultBinding
import jp.microvent.microvent.viewModel.VentilatorResultViewModel
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel

class VentilatorResultFragment : Fragment() {

    private val args: VentilatorResultFragmentArgs by navArgs()

    private val ventilatorResultViewModel by lazy {
        ViewModelProvider(this, VentilatorResultViewModel.Factory(
            requireActivity().application, args.ventilatorValue
        )).get(VentilatorResultViewModel::class.java)
    }

    private lateinit var binding: FragmentVentilatorResultBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ventilator_result, container, false)

        val viewModel = ventilatorResultViewModel

        binding.apply {
            ventilatorResultViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        ventilatorResultViewModel.transitionToVentilatorSetting.observe(
            viewLifecycleOwner, Observer {
                val patient = ventilatorResultViewModel.patient
                val action = VentilatorResultFragmentDirections.actionVentilatorResultToVentilatorSetting(patient)
                findNavController().navigate(action)
            }
        )

        return binding.root
    }
}