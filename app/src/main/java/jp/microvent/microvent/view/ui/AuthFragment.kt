package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentAuthBinding
import jp.microvent.microvent.view.permission.AccessLocationPermission
import jp.microvent.microvent.view.ui.dialog.DialogConfirmLogoutOnAnotherTerminalFragment
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.AuthViewModel
import jp.microvent.microvent.viewModel.util.Event
import jp.microvent.microvent.viewModel.util.EventObserver
import java.lang.Exception

class AuthFragment : BaseFragment(),DialogConfirmLogoutOnAnotherTerminalFragment.DialogconfirmLogoutOnAnotherTerminalListener {

    override val viewModel by viewModels<AuthViewModel>()

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)

        binding.apply {
            authViewModel = viewModel
            //LiveDataの変更をviewに通知するために必要
            lifecycleOwner = viewLifecycleOwner
        }

        if (AccessLocationPermission.hasPermission(requireActivity())) {
            viewModel.setLocation()
        }

        viewModel.apply {

            transitionToQrReading.observe(
                viewLifecycleOwner,
                EventObserver {
                    findNavController().navigate(R.id.action_auth_to_qr_reading)
                }
            )

            transitionToPatientSetting.observe(
                viewLifecycleOwner,
                EventObserver {
                    findNavController().navigate(R.id.action_auth_to_patient_setting)
                }
            )

            transitionToVentilatorSetting.observe(
                viewLifecycleOwner,
                EventObserver {
                    findNavController().navigate(R.id.action_auth_to_ventilator_setting)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = AuthFragmentDirections.actionToHelp(getString(R.string.auth_manual_path))
                    findNavController().navigate(action)
                }
            )

            /**
             * 別端末ログイン中の場合のダイアログ表示
             */
            showDialogConfirmLogoutOnAnotherTerminal.observe(
                viewLifecycleOwner,
                EventObserver {
                    val dialog = DialogConfirmLogoutOnAnotherTerminalFragment()
                    dialog.show(parentFragmentManager, it)
                }
            )
        }


        return binding.root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

}