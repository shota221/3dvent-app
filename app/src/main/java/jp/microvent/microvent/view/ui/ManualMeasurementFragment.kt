package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentManualMeasurementBinding
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.ManualMeasurementViewModel
import jp.microvent.microvent.viewModel.VentilatorResultViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class ManualMeasurementFragment : DrawableFragment() {

    private val args: ManualMeasurementFragmentArgs by navArgs()

    override val viewModel by lazy {
        ViewModelProvider(
            this, ManualMeasurementViewModel.Factory(
                requireActivity().application, args.ventilatorValue
            )
        ).get(ManualMeasurementViewModel::class.java)
    }

    private lateinit var binding: FragmentManualMeasurementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manual_measurement,
            container,
            false
        )

        binding.apply {
            manualMeasurementViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.apply {
            transitionToVentilatorResult.observe(
                viewLifecycleOwner, EventObserver {
                    val ventilatorValue = viewModel.ventilatorValue
                    val action =
                        ManualMeasurementFragmentDirections.actionManualMeasurementToVentilatorResult(
                            ventilatorValue
                        )
                    findNavController().navigate(action)
                }
            )

            transitionToHelp.observe(
                viewLifecycleOwner, EventObserver{
                    val action = ManualMeasurementFragmentDirections.actionToHelp(getString(R.string.manual_measurement_manual_path))
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

    override fun onStop() {
        super.onStop()
        hideBackButton()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_manual_measurement_pop)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}