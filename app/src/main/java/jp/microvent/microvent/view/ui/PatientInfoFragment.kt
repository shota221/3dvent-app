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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentPatientInfoBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.view.ui.dialog.DialogNoPatientLinkedFragment
import jp.microvent.microvent.viewModel.PatientInfoViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientInfoFragment : BaseFragment(),DialogNoPatientLinkedFragment.DialogNoPatientLinkedListener {

    private val viewModel by viewModels<PatientInfoViewModel>()

    private lateinit var binding: FragmentPatientInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_info, container, false)

        binding.apply {
            patientInfoViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply{
            transitionToPatientBasicInfo.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_info_to_patient_basic_info_detail)
                }
            )
            transitionToPatientObsData.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_info_to_patient_obs_data_detail)
                }
            )
            transitionToMeasurementData.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_info_to_measurement_data)
                }
            )
            transitionToVentilatorData.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_info_to_ventilator_data_detail)
                }
            )
            transitionToQrReading.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_patient_info_to_qr_reading)
                }
            )
            showDialogNoPatientLinked.observe(
                viewLifecycleOwner,EventObserver{
                    val dialog = DialogNoPatientLinkedFragment()
                    dialog.show(requireActivity().supportFragmentManager, it)
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

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

}