package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import jp.microvent.microvent.R
import jp.microvent.microvent.service.AppWebViewClientCallBack

abstract class WebViewBaseFragment : BaseFragment(), AppWebViewClientCallBack {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPageStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onPageCommitVisible() {
        progressBar.visibility = View.GONE
    }

    override fun onReceivedError() {
        Toast.makeText(requireActivity(), getString(R.string.network_error), Toast.LENGTH_LONG).show()
    }

    override fun onReceivedHttpError() {
        Toast.makeText(requireActivity(), getString(R.string.unexpected_error), Toast.LENGTH_LONG).show()
    }
}