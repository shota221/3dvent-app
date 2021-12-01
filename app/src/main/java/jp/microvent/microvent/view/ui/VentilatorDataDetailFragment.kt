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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentVentilatorDataDetailBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.VentilatorDataDetailViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorDataDetailFragment : BaseFragment() {

    override val viewModel by viewModels<VentilatorDataDetailViewModel>()

    private lateinit var binding: FragmentVentilatorDataDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ventilator_data_detail, container, false)

        binding.apply {
            ventilatorDataDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply{
            transitionToVentilatorDataUpdate.observe(
                viewLifecycleOwner,EventObserver{
                    viewModel.ventilator.value?.let { ventilator->
                        val action =
                            VentilatorDataDetailFragmentDirections.actionVentilatorDataDetailToUpdate(
                                ventilator
                            )
                        findNavController().navigate(action)
                    }
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
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                }
            )

            /**
             * プログレスバー制御
             */
            setProgressBar.observe(
                viewLifecycleOwner,
                EventObserver {
                    progressBar.visibility = if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            )
        }

        return binding.root
    }

}