package jp.microvent.microvent.view.ui

import android.content.Intent
import android.net.Uri
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
import jp.microvent.microvent.viewModel.ChatViewModel
import jp.microvent.microvent.viewModel.PatientInfoViewModel
import jp.microvent.microvent.viewModel.SupportViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class SupportFragment : BaseFragment(),DialogNoPatientLinkedFragment.DialogNoPatientLinkedListener {

    override val viewModel by viewModels<SupportViewModel>()
    val chatViewModel by viewModels<ChatViewModel>()

    private lateinit var binding: FragmentSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)

        binding.apply {
            supportViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transactionToTextManual.observe(
                viewLifecycleOwner, EventObserver {
                    val action = SupportFragmentDirections.actionSupportToTextManual(getString(R.string.locale))
                    findNavController().navigate(action)
                }
            )
            transactionToVideoManual.observe(
                viewLifecycleOwner, EventObserver {
                    val action = SupportFragmentDirections.actionSupportToVideoManual(getString(R.string.locale))
                    findNavController().navigate(action)
                }
            )
            transactionToBugReport.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_support_to_bug_report)
                }
            )
            transactionToChat.observe(
                viewLifecycleOwner, EventObserver {
//                    findNavController().navigate(R.id.action_support_to_chat)
                    chatViewModel.roomUri.value?.let{
                        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        startActivity(intent)
                    }
                }
            )
        }

        return binding.root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

}