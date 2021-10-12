package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentBugReportBinding
import jp.microvent.microvent.view.ui.dialog.DialogNotificationFragment
import jp.microvent.microvent.viewModel.BugReportViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class BugReportFragment : BaseFragment(),DialogNotificationFragment.DialogNotificationListener {

    private val viewModel by viewModels<BugReportViewModel>()

    private lateinit var binding: FragmentBugReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bug_report,
            container,
            false
        )

        binding.apply {
            bugReportViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            showDialogNotification.observe(
                viewLifecycleOwner, EventObserver {
                    val dialog = DialogNotificationFragment(this@BugReportFragment, it)
                    dialog.show(requireActivity().supportFragmentManager, it.title)
                }
            )
        }

        return binding.root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.onClickDialogPositive()
        binding.etBugName.requestFocus()
    }
}