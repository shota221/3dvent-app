package jp.microvent.microvent.view.ui

import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentTestBinding
import jp.microvent.microvent.databinding.FragmentVentilatorDataUpdateBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.view.ui.dialog.DialogDatePickerFragment
import jp.microvent.microvent.view.ui.dialog.DialogTimePickerFragment
import jp.microvent.microvent.viewModel.VentilatorDataUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorDataUpdateFragment : BaseFragment() {

    private val args: VentilatorDataUpdateFragmentArgs by navArgs()

    override val viewModel by lazy {
        ViewModelProvider(
            this, VentilatorDataUpdateViewModel.Factory(
                requireActivity().application, args.ventilator
            )
        ).get(VentilatorDataUpdateViewModel::class.java)
    }

    private lateinit var binding: FragmentVentilatorDataUpdateBinding

    private val datePickerDialog by lazy {
        DialogDatePickerFragment()
    }

    private val timePickerDialog by lazy {
        DialogTimePickerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ventilator_data_update,
            container,
            false
        )

        binding.apply {
            ventilatorDataUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            /**
             * フォーカスがあたった時に表示
             */
            etStartUsingAt.inputType = InputType.TYPE_NULL
            etStartUsingAt.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    datePickerDialog.show(requireActivity().supportFragmentManager, "datePicker")
                }
            }
        }

        viewModel.apply {
            transitionToVentilatorDataDetail.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_ventilator_data_update_to_detail)
                }
            )

            /**
             * フォーカスがあたってダイアログがキャンセルされた際、再度クリックで表示
             */
            showDialogDatePicker.observe(
                viewLifecycleOwner, EventObserver{
                    datePickerDialog.show(requireActivity().supportFragmentManager, "datePicker")
                }
            )
        }

        datePickerDialog.dateString.observe(
            viewLifecycleOwner, Observer{
                viewModel.startUsingAt.value = it
                timePickerDialog.show(requireActivity().supportFragmentManager, "timePicker")
            }
        )

        timePickerDialog.timeString.observe(
            viewLifecycleOwner, Observer {
                val startUsingAtStr = viewModel.startUsingAt.value + " " + it
                viewModel.startUsingAt.value = startUsingAtStr
            }
        )

        return binding.root
    }
}