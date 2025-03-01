package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorResultBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.VentilatorResultViewModel
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorResultFragment : DrawableFragment() {

    private val args: VentilatorResultFragmentArgs by navArgs()

    override val viewModel by lazy {
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

        binding.apply {
            ventilatorResultViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToVentilatorSetting.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_ventilator_result_to_ventilator_setting)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = VentilatorResultFragmentDirections.actionToHelp(getString(R.string.ventilator_result_manual_path))
                    findNavController().navigate(action)
                }
            )

            showFlowDrawer.observe(
                viewLifecycleOwner, EventObserver{
                    showFlowDrawer(R.id.point_flow_ventilator_result)
                }
            )
        }
        return binding.root
    }
}