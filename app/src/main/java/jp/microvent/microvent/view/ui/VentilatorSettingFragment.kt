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
import jp.microvent.microvent.databinding.FragmentVentilatorSettingBinding
import jp.microvent.microvent.viewModel.AuthViewModel
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel

class VentilatorSettingFragment : Fragment() {

    private val args: VentilatorSettingFragmentArgs by navArgs()

    private val ventilatorSettingViewModel by lazy {
        ViewModelProvider(this, VentilatorSettingViewModel.Factory(
            requireActivity().application, args.height, args.gender, args.predictedVt
        )).get(VentilatorSettingViewModel::class.java)
    }

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
            viewLifecycleOwner, Observer {
                val action =
                findNavController()
            }
        )
        return binding.root
    }

}