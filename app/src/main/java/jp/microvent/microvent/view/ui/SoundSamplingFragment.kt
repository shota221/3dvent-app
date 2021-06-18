package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentSoundMeasurementTestBinding
import jp.microvent.microvent.databinding.FragmentSoundSamplingBinding
import jp.microvent.microvent.view.permission.RecordAudioPermission
import jp.microvent.microvent.viewModel.SoundSamplingViewModel

class SoundSamplingFragment : Fragment() {


    private val soundSamplingViewModel by viewModels<SoundSamplingViewModel>()

    private lateinit var binding: FragmentSoundMeasurementTestBinding

    private val launcher = registerForActivityResult(
        RecordAudioPermission.RequestContract(), ::onPermissionResult
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!RecordAudioPermission.hasPermission(requireActivity())) {
            launcher.launch(Unit)
        }

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sound_measurement_test, container, false)

        val viewModel = soundSamplingViewModel

        binding.apply {
            soundSamplingViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    private fun onPermissionResult(granted: Boolean) {
        if (granted) {
        } else {
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        }
    }
}