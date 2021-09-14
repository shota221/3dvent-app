package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import jp.microvent.microvent.databinding.FragmentPatientObsDataUpdateBinding
import jp.microvent.microvent.databinding.FragmentTestBinding
import jp.microvent.microvent.service.enum.*
import jp.microvent.microvent.view.adapter.SpinnerBinder
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.PatientObsDataUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientObsDataUpdateFragment : BaseFragment() {

    private val args: PatientObsDataUpdateFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(
            this, PatientObsDataUpdateViewModel.Factory(
                requireActivity().application, args.patientObs
            )
        ).get(PatientObsDataUpdateViewModel::class.java)
    }

    private val spinnerBinder by lazy {
        SpinnerBinder(requireContext())
    }

    private lateinit var binding: FragmentPatientObsDataUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_patient_obs_data_update,
            container,
            false
        )

        binding.apply {
            patientObsDataUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner

            spinnerBinder.bind(
                spOptOutFlg,
                OptOutFlg.getStringList(requireContext()),
                viewModel::onOptOutFlgSelected,
                viewModel.patientObs.optOutFlg
            )
            spinnerBinder.bind(
                spUsedPlace,
                UsedPlace.getStringList(requireContext()),
                viewModel::onUsedPlaceSelected,
                viewModel.patientObs.usedPlace
            )
            spinnerBinder.bind(
                spTreatment,
                Treatment.getStringList(requireContext()),
                viewModel::onTreatmentSelected,
                viewModel.patientObs.treatment
            )
            spinnerBinder.bind(
                spOutcome,
                Outcome.getStringList(requireContext()),
                viewModel::onOutcomeSelected,
                viewModel.patientObs.outcome
            )
            spinnerBinder.bind(
                spAdverseEventFlg,
                AdverseEventFlg.getStringList(requireContext()),
                viewModel::onAdverseEventFlgSelected,
                viewModel.patientObs.adverseEventFlg
            )
        }

        viewModel.apply {
            transitionToPatientObsDataDetail.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_patient_obs_update_to_detail)
                }
            )


            transitionToAuth.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_to_auth)
                }
            )

            /**
             * 通信エラーダイアログの表示
             */
            showDialogConnectionError.observe(
                viewLifecycleOwner,
                EventObserver {
                    val dialog = DialogConnectionErrorFragment()
                    dialog.show(requireActivity().supportFragmentManager, it)
                }
            )

            /**
             * トースト表示
             */
            showToast.observe(
                viewLifecycleOwner,
                EventObserver {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                }
            )

            /**
             * プログレスバー制御
             */
            setProgressBar.observe(
                viewLifecycleOwner,
                EventObserver {
                    progressBar.visibility = if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            )
        }

        return binding.root
    }
}