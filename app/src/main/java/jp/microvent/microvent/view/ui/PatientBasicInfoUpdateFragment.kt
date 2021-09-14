package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentPatientBasicInfoUpdateBinding
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.view.adapter.SpinnerBinder
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.ManualMeasurementViewModel
import jp.microvent.microvent.viewModel.PatientBasicInfoUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientBasicInfoUpdateFragment : BaseFragment() {

    private val args: PatientBasicInfoUpdateFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(
            this, PatientBasicInfoUpdateViewModel.Factory(
                requireActivity().application, args.patient
            )
        ).get(PatientBasicInfoUpdateViewModel::class.java)
    }

    private val spinnerBinder by lazy {
        SpinnerBinder(requireContext())
    }

    private lateinit var binding: FragmentPatientBasicInfoUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_patient_basic_info_update,
                container,
                false
            )

        binding.apply {
            patientBasicInfoUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner

            spinnerBinder.bind(
                spGender,
                Gender.getStringList(requireContext()),
                viewModel::onItemSelected,
                viewModel.patient.gender
            )
        }

        viewModel.apply {
            transitionToPatientBasicInfoDetail.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_patient_basic_info_update_to_detail)
                }
            )

            transitionToAuth.observe(
                viewLifecycleOwner, Observer {
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