package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorResultBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.VentilatorResultViewModel
import jp.microvent.microvent.viewModel.VentilatorSettingViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorResultFragment : Fragment() {

    private val args: VentilatorResultFragmentArgs by navArgs()

    private val ventilatorResultViewModel by lazy {
        ViewModelProvider(this, VentilatorResultViewModel.Factory(
            requireActivity().application, args.ventilatorValue
        )).get(VentilatorResultViewModel::class.java)
    }

    private lateinit var binding: FragmentVentilatorResultBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ventilator_result, container, false)

        val viewModel = ventilatorResultViewModel

        binding.apply {
            ventilatorResultViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        ventilatorResultViewModel.apply {
            transitionToVentilatorSetting.observe(
                viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_ventilator_result_to_ventilator_setting)
                }
            )

            transitionToAuth.observe(
                viewLifecycleOwner, EventObserver {
                    findNavController().navigate(R.id.action_to_auth)
                }
            )

            /**
             * 通信エラーダイアログの表示
             */
            showDialogConnectionError.observe(
                viewLifecycleOwner,
                EventObserver {
                    val dialog = DialogConnectionErrorFragment()
                    dialog.show(requireActivity().supportFragmentManager, it)
                }
            )

            /**
             * トースト表示
             */
            showToast.observe(
                viewLifecycleOwner,
                EventObserver {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            )
        }
        return binding.root
    }
}