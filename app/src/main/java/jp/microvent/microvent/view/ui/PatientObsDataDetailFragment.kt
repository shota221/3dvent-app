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
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentPatientObsDataDetailBinding
import jp.microvent.microvent.databinding.FragmentTestBinding
import jp.microvent.microvent.viewModel.PatientObsDataDetailViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientObsDataDetailFragment : Fragment() {

    private val viewModel by viewModels<PatientObsDataDetailViewModel>()

    private lateinit var binding: FragmentPatientObsDataDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_obs_data_detail, container, false)

        binding.apply {
            patientObsDataDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply{
            transitionToPatientObsDataUpdate.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_obs_detail_to_update)
                }
            )
        }

        return binding.root
    }

}