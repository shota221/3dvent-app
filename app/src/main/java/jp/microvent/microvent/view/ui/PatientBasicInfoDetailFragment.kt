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
import jp.microvent.microvent.databinding.FragmentPatientBasicInfoDetailBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.PatientBasicInfoDetailViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientBasicInfoDetailFragment : BaseFragment() {

    override val viewModel by viewModels<PatientBasicInfoDetailViewModel>()

    private lateinit var binding: FragmentPatientBasicInfoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_patient_basic_info_detail,
            container,
            false
        )

        binding.apply {
            patientBasicInfoDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToPatientBasicInfoUpdate.observe(
                viewLifecycleOwner, EventObserver {
                    viewModel.patient.value?.let { patient->
                        val action =
                            PatientBasicInfoDetailFragmentDirections.actionPatientBasicInfoDetailToUpdate(
                                patient
                            )
                        findNavController().navigate(action)
                    }
                }
            )
        }

        return binding.root
    }

}