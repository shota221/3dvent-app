package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentTestBinding
import jp.microvent.microvent.databinding.FragmentVentilatorDataUpdateBinding
import jp.microvent.microvent.viewModel.VentilatorDataUpdateViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class VentilatorDataUpdateFragment : Fragment() {

    private val viewModel by viewModels<VentilatorDataUpdateViewModel>()

    private lateinit var binding: FragmentVentilatorDataUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ventilator_data_update, container, false)

        binding.apply {
            ventilatorDataUpdateViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToVentilatorDataDetail.observe(
                viewLifecycleOwner, EventObserver{
                    findNavController().navigate(R.id.action_ventilator_data_update_to_detail)
                }
            )
        }

        return binding.root
    }

}