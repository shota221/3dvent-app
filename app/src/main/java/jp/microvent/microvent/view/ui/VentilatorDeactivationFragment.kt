package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorDeactivationBinding
import jp.microvent.microvent.viewModel.VentilatorDeactivationViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorDeactivationFragment : BaseFragment() {
    override val viewModel by viewModels<VentilatorDeactivationViewModel>()

    private lateinit var binding: FragmentVentilatorDeactivationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ventilator_deactivation, container, false)

        binding.apply {
            ventilatorDeactivationViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            transitionToQrReading.observe(
                viewLifecycleOwner, EventObserver{
                    findNavController().navigate(R.id.action_ventilator_deactivation_to_qr_reading)
                }
            )
        }
    }

    override fun onDialogClick(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.deactivateVentilator()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}