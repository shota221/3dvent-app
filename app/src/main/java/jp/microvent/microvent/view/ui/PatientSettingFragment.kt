package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentPatientSettingBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.PatientSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class PatientSettingFragment : Fragment() {

    private val patientSettingViewModel by viewModels<PatientSettingViewModel>()

    private lateinit var binding: FragmentPatientSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_patient_setting, container, false)

        val viewModel = patientSettingViewModel

        binding.apply {
            patientSettingViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        //スピナーdatabinding用のadapterを準備
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_list,
            android.R.layout.simple_spinner_item
        )

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

        patientSettingViewModel.transitionToVentilatorSetting.observe(
            viewLifecycleOwner, Observer {
                findNavController().navigate(R.id.action_patient_setting_to_ventilator_setting)
            }
        )

        patientSettingViewModel.showDialogConnectionError.observe(
            viewLifecycleOwner, EventObserver {
                val dialog = DialogConnectionErrorFragment()
                dialog.show(requireActivity().supportFragmentManager, it)
            }
        )

        return binding.root
    }
}