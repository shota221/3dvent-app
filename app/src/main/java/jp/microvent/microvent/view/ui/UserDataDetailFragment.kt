package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.EventLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentUserDataDetailBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.UserDataDetailViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class UserDataDetailFragment : BaseFragment() {

    override val viewModel by viewModels<UserDataDetailViewModel>()

    private lateinit var binding: FragmentUserDataDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_data_detail,container,false)

        binding.apply{
            userDataDetailVeiwModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply{
            transitionToUserDataUpdate.observe(
                viewLifecycleOwner, EventObserver{
                    viewModel.user.value?.let {
                        val action = UserDataDetailFragmentDirections.actionUserDataDetailToUpdate(it)
                        findNavController().navigate(action)
                    }
                }
            )
        }

        return binding.root
    }
}