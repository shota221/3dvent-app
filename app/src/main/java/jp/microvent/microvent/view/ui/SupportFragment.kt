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
import jp.microvent.microvent.databinding.FragmentSupportBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.view.ui.dialog.DialogNoPatientLinkedFragment
import jp.microvent.microvent.viewModel.PatientInfoViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class SupportFragment : BaseFragment(),DialogNoPatientLinkedFragment.DialogNoPatientLinkedListener {

    private lateinit var binding: FragmentSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)

        return binding.root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

}