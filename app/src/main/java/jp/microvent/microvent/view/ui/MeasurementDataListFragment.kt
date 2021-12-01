package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentMeasurementDataListBinding
import jp.microvent.microvent.view.adapter.MeasurementDataAdapter
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.MeasurementDataListViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class MeasurementDataListFragment : BaseFragment() {

    companion object {
        fun newInstance() = MeasurementDataListFragment()
    }

    override val viewModel by viewModels<MeasurementDataListViewModel>()

    //    private lateinit var ventilatorValueListAdapter: MeasurementDataAdapter
    private val measurementDataListAdapter: MeasurementDataAdapter by lazy {
        MeasurementDataAdapter(viewModel)
    }

    private lateinit var binding: FragmentMeasurementDataListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeasurementDataListBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            measurementDataListViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner

            lsMeasurementData.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter = measurementDataListAdapter
            }
        }


        viewModel.apply {
            ventilatorValueList.observe(
                viewLifecycleOwner, {
                    measurementDataListAdapter.setMeasurementDataList(it)
                }
            )
            transitionToMeasurementDataDetail.observe(
                viewLifecycleOwner, EventObserver {
                    viewModel.ventilatorValueId.value?.let {
                        val action =
                            MeasurementDataListFragmentDirections.actionMeasurementDataListToDetail(
                                it
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