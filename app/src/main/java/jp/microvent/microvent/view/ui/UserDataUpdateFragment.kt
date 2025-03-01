package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentUserDataUpdateBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.UserDataUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class UserDataUpdateFragment : BaseFragment() {

    private val args: UserDataUpdateFragmentArgs by navArgs()

    override val viewModel by lazy {
        ViewModelProvider(
            this, UserDataUpdateViewModel.Factory(
                requireActivity().application, args.user
            )
        ).get(UserDataUpdateViewModel::class.java)
    }

    private lateinit var binding : FragmentUserDataUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_data_update,
            container,
            false
        )

        binding.apply {
            userDataUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply{
            transitionToUserDataDetail.observe(
                viewLifecycleOwner,EventObserver{
                    findNavController().navigate(R.id.action_user_data_update_to_detail)
                }
            )
        }

        return binding.root
    }
}