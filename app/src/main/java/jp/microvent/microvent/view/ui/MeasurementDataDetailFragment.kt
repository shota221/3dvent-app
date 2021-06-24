package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentMeasurementDataDetailBinding
import jp.microvent.microvent.viewModel.MeasurementDataDetailViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class MeasurementDataDetailFragment : Fragment() {

    private val args: MeasurementDataDetailFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(
            this, MeasurementDataDetailViewModel.Factory(
                requireActivity().application, args.ventilatorValueId
            )
        ).get(MeasurementDataDetailViewModel::class.java)
    }

    private lateinit var binding: FragmentMeasurementDataDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_measurement_data_detail, container, false)

        binding.apply {
            measurementDataDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToMeasurementDataUpdate.observe(
                viewLifecycleOwner, EventObserver {
                    viewModel.ventilatorValue.value?.let {
                        val action =
                            MeasurementDataDetailFragmentDirections.actionMeasurementDataDetailToUpdate(
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