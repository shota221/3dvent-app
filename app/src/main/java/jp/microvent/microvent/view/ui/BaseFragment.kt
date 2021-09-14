package jp.microvent.microvent.view.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import jp.microvent.microvent.R

open class BaseFragment : Fragment() {
    protected val progressBar: ProgressBar by lazy{
        requireActivity().findViewById(R.id.progressBar)
    }

    protected val softKeyBoard: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onStop() {
        super.onStop()
        //プログレスバー消去
        progressBar.visibility = View.GONE
        //キーボード非表示
        softKeyBoard.hideSoftInputFromWindow(requireActivity().findViewById<ConstraintLayout>(R.id.mainLayout).windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}