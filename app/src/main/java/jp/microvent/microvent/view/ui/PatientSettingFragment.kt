package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentPatientSettingBinding
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.PatientSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientSettingFragment : DrawableFragment() {

    override val viewModel by viewModels<PatientSettingViewModel>()

    private lateinit var binding: FragmentPatientSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_setting, container, false)

        binding.apply {
            patientSettingViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, Gender.getStringList(requireContext()))

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spGender.apply {
            setAdapter(adapter)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onItemSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }

        viewModel.apply {
            transitionToVentilatorSetting.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_patient_setting_to_ventilator_setting)
                }
            )

            transitionToAuth.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_to_auth)
                }
            )

            transitionToQrReading.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_patient_setting_to_qr_reading)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = PatientSettingFragmentDirections.actionToHelp(getString(R.string.patient_setting_manual_path))
                    findNavController().navigate(action)
                }
            )

            /**
             *　ドロワーボタン監視
             */
            showFlowDrawer.observe(
                viewLifecycleOwner, EventObserver{
                    showFlowDrawer(R.id.point_flow_patient_setting)
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