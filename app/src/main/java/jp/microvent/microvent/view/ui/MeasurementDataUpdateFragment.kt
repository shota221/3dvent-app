package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentMeasurementDataBinding
import jp.microvent.microvent.databinding.FragmentMeasurementDataUpdateBinding
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.service.enum.StatusUse
import jp.microvent.microvent.view.adapter.SpinnerBinder
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.MeasurementDataUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class MeasurementDataUpdateFragment : BaseFragment() {

    private val args: MeasurementDataUpdateFragmentArgs by navArgs()

    override val viewModel by lazy {
        ViewModelProvider(
            this, MeasurementDataUpdateViewModel.Factory(
                requireActivity().application, args.ventilatorValue
            )
        ).get(MeasurementDataUpdateViewModel::class.java)
    }

    private val spinnerBinder by lazy {
        SpinnerBinder(requireContext())
    }

    private lateinit var binding: FragmentMeasurementDataUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_measurement_data_update,
            container,
            false
        )

        binding.apply {
            measurementDataUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner

            spinnerBinder.bind(
                spGender,
                Gender.getStringList(requireContext()),
                viewModel::onGenderSelected,
                viewModel.ventilatorValue.gender
            )
            spinnerBinder.bind(
                spStatusUse,
                StatusUse.getStringList(requireContext()),
                viewModel::onStatusUseSelected,
                viewModel.ventilatorValue.statusUse
            )
        }

        viewModel.apply {
            transitionToMeasurementDataDetail.observe(
                viewLifecycleOwner, EventObserver {
                    viewModel.updatedVentilatorValueId.value?.let {
                        val action =
                            MeasurementDataUpdateFragmentDirections.actionMeasurementDataUpdateToDetail(
                                it
                            )
                        findNavController().navigate(action)
                    }
                }
            )
        }

        return binding.root
    }

}