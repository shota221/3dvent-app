package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentLogoutBinding
import jp.microvent.microvent.viewModel.LogoutViewModel

class LogoutFragment : BaseFragment() {
    override val viewModel by viewModels<LogoutViewModel>()

    private lateinit var binding: FragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logout, container, false)

        binding.apply {
            logoutViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }
}