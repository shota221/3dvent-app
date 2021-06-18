package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentSoundMeasurementBinding
import jp.microvent.microvent.view.permission.RecordAudioPermission
import jp.microvent.microvent.viewModel.SoundMeasurementViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class SoundMeasurementFragment : Fragment() {

    private val args: SoundMeasurementFragmentArgs by navArgs()

    private val soundMeasurementViewModel by lazy {
        ViewModelProvider(
            this, SoundMeasurementViewModel.Factory(
                requireActivity().application, args.ventilatorValue
            )
        ).get(SoundMeasurementViewModel::class.java)
    }

    private lateinit var binding: FragmentSoundMeasurementBinding

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_sound_measurement, container, false)

        val viewModel = soundMeasurementViewModel

        binding.apply {
            soundMeasurementViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        soundMeasurementViewModel.transitionToManualMeasurement.observe(
            viewLifecycleOwner, EventObserver {
                val ventilatorValue = soundMeasurementViewModel.ventilatorValue
                val action =
                    SoundMeasurementFragmentDirections.actionSoundMeasurementToManualMeasurement(
                        ventilatorValue
                    )
                findNavController().navigate(action)
            }
        )

        soundMeasurementViewModel.transitionToVentilatorResult.observe(
            viewLifecycleOwner, EventObserver {
                val ventilatorValue = soundMeasurementViewModel.ventilatorValue
                val action =
                    SoundMeasurementFragmentDirections.actionSoundMeasurementToVentilatorResult(
                        ventilatorValue
                    )
                findNavController().navigate(action)
            }
        )

        setupBackButton()
        setHasOptionsMenu(true)

        return binding.root
    }

    //他のページに戻るボタンを残さない
    override fun onStop() {
        super.onStop()
        hideBackButton()
    }

    private fun onPermissionResult(granted: Boolean) {
        if (granted) {
        } else {
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        }
    }

    fun setupBackButton() {
        val activity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideBackButton() {
        val activity = activity as AppCompatActivity
        val actionBar: ActionBar? = activity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    //戻るボタンを押されたときの遷移処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                hideBackButton()
                findNavController().navigate(R.id.action_sound_measurement_pop)
                true
            }
            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}