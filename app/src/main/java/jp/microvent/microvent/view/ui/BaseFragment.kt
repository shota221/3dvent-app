package jp.microvent.microvent.view.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.view.ui.dialog.DialogConfirmationFragment
import jp.microvent.microvent.view.ui.dialog.DialogNotificationFragment
import jp.microvent.microvent.viewModel.BaseViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

open abstract class BaseFragment : Fragment(), DialogNotificationFragment.DialogNotificationListener, DialogConfirmationFragment.DialogConfirmationListener {
    abstract val viewModel: BaseViewModel

    protected val progressBar: ProgressBar by lazy{
        requireActivity().findViewById(R.id.progressBar)
    }

    protected val softKeyBoard: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            //トースト表示
            showToast.observe(
                viewLifecycleOwner, EventObserver{
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                }
            )
            //通知ダイアログ表示
            showDialogNotification.observe(
                viewLifecycleOwner, EventObserver{
                    val dialog = DialogNotificationFragment(this@BaseFragment, it)
                    dialog.show(requireActivity().supportFragmentManager, it.title)
                }
            )
            //確認ダイアログ表示
            showDialogConfirmation.observe(
                viewLifecycleOwner, EventObserver{
                    val dialog = DialogConfirmationFragment(this@BaseFragment, it)
                    dialog.show(requireActivity().supportFragmentManager, it.title)
                }
            )

            //auth画面遷移
            transitionToAuth.observe(
                viewLifecycleOwner, EventObserver{
                    findNavController().navigate(R.id.action_to_auth)
                }
            )

            //プログレスバー表示
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
    }


    override fun onStop() {
        super.onStop()
        //プログレスバー消去
        progressBar.visibility = View.GONE
        //キーボード非表示
        softKeyBoard.hideSoftInputFromWindow(requireActivity().findViewById<ConstraintLayout>(R.id.mainLayout).windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onDialogClick(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }
}