package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentSettingBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.SettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class SettingFragment : BaseFragment() {
    override val viewModel by viewModels<SettingViewModel>()

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        binding.apply {
            settingViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToUserData.observe(
                viewLifecycleOwner,
                EventObserver {
                    findNavController().navigate(R.id.action_setting_to_user_data_detail)
                }
            )

            transitionToVentilatorDeactivation.observe(
                viewLifecycleOwner,
                EventObserver {
                    findNavController().navigate(R.id.action_setting_to_ventilator_deactivation)
                }
            )
        }
        return binding.root
    }


}