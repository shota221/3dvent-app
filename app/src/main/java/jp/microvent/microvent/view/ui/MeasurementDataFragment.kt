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
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentMeasurementDataBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.MeasurementDataViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class MeasurementDataFragment : BaseFragment() {

    override val viewModel by viewModels<MeasurementDataViewModel>()

    private lateinit var binding: FragmentMeasurementDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_measurement_data, container, false)

        binding.apply {
            measurementDataViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToLatestMeasurementData.observe(
                viewLifecycleOwner, EventObserver {
                    viewModel.ventilatorValueId.value?.let {
                        val action =
                            MeasurementDataFragmentDirections.actionMeasurementDataToDetail(
                                it
                            )
                        findNavController().navigate(action)
                    }
                }
            )

            transitionToPrevMeasurementList.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_measurement_data_to_list)
                }
            )
        }

        return binding.root
    }

}