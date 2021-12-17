package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentTextManualBinding
import jp.microvent.microvent.service.AppWebViewClient
import jp.microvent.microvent.viewModel.TextManualViewModel

private const val BASE_URL = "https://manual.microvent.r102.jp/text/"
//private const val BASE_URL = "http://manual.microvent.local/text/"

class TextManualFragment : WebViewBaseFragment(), OnBackKeyPressedListener {
    override val viewModel by viewModels<TextManualViewModel>()

    private val args: TextManualFragmentArgs by navArgs()

    private lateinit var binding: FragmentTextManualBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_text_manual,
            container,
            false
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = BASE_URL + args.locale

        binding.textManualWebView.apply {
            webViewClient = AppWebViewClient(this@TextManualFragment)

            settings.javaScriptEnabled = true

            loadUrl(url)
        }
    }

    override fun onBackPressed() {
        parentFragmentManager.popBackStack()
    }

    override fun onReceivedError() {
        super.onReceivedError()
        binding.textManualWebView.visibility = View.GONE
    }

    override fun onReceivedHttpError() {
        super.onReceivedHttpError()
        binding.textManualWebView.visibility = View.GONE
    }
}