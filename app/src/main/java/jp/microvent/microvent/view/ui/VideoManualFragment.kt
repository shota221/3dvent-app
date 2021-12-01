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
import jp.microvent.microvent.databinding.FragmentVideoManualBinding
import jp.microvent.microvent.service.AppWebViewClient
import jp.microvent.microvent.viewModel.VideoManualViewModel

private const val BASE_URL = "http://manual.microvent.local/video/"

class VideoManualFragment : WebViewBaseFragment(), OnBackKeyPressedListener {
    override val viewModel by viewModels<VideoManualViewModel>()

    private val args: VideoManualFragmentArgs by navArgs()

    private lateinit var binding: FragmentVideoManualBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_video_manual,
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

        binding.videoManualWebView.apply {
            webViewClient = AppWebViewClient(this@VideoManualFragment)

            settings.javaScriptEnabled = true

            loadUrl(url)
        }
    }

    override fun onBackPressed() {
        parentFragmentManager.popBackStack()
    }

    override fun onReceivedError() {
        super.onReceivedError()
        binding.videoManualWebView.visibility = View.GONE
    }

    override fun onReceivedHttpError() {
        super.onReceivedHttpError()
        binding.videoManualWebView.visibility = View.GONE
    }
}