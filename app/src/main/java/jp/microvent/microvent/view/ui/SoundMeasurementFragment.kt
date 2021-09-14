package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.SoundMeasurementViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class SoundMeasurementFragment : DrawableFragment() {

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

        soundMeasurementViewModel.apply {
            transitionToManualMeasurement.observe(
                viewLifecycleOwner, EventObserver {
                    val ventilatorValue = soundMeasurementViewModel.ventilatorValue
                    val action =
                        SoundMeasurementFragmentDirections.actionSoundMeasurementToManualMeasurement(
                            ventilatorValue
                        )
                    findNavController().navigate(action)
                }
            )

            transitionToVentilatorResult.observe(
                viewLifecycleOwner, EventObserver {
                    val ventilatorValue = soundMeasurementViewModel.ventilatorValue
                    val action =
                        SoundMeasurementFragmentDirections.actionSoundMeasurementToVentilatorResult(
                            ventilatorValue
                        )
                    findNavController().navigate(action)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = SoundMeasurementFragmentDirections.actionToHelp(getString(R.string.sound_measurement_manual_path))
                    findNavController().navigate(action)
                }
            )

            showFlowDrawer.observe(
                viewLifecycleOwner, EventObserver{
                    showFlowDrawer(R.id.point_flow_ie_measurement)
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
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}