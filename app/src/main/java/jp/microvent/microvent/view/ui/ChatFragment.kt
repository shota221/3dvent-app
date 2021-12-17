package jp.microvent.microvent.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentChatBinding
import jp.microvent.microvent.service.AppWebViewClient
import jp.microvent.microvent.service.AppWebViewClientCallBack
import jp.microvent.microvent.viewModel.ChatViewModel

class ChatFragment : WebViewBaseFragment() {
    override val viewModel by viewModels<ChatViewModel>()

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )

        binding.apply {
            chatViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.apply {
//            roomUri.observe(
//                viewLifecycleOwner, Observer {
//                    binding.chatWebView.apply {
//                        webViewClient = AppWebViewClient(this@ChatFragment)
//
//                        settings.javaScriptEnabled = true
//
//                        loadUrl("https://chat.microvent.local/index.php/call/5b2xm2oe")
//                    }
//                }
//            )
//        }

        binding.chatWebView.apply {
            webViewClient = AppWebViewClient(this@ChatFragment)

            settings.javaScriptEnabled = true

            viewModel.roomUri.value?.let {
                //todo:webview表示。なぜかレンダリングが途中で止まる
//                loadUrl(it)
                val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
    }

    override fun onReceivedError() {
        super.onReceivedError()
        binding.chatWebView.visibility = View.GONE
    }

    override fun onReceivedHttpError() {
        super.onReceivedHttpError()
        binding.chatWebView.visibility = View.GONE
    }
}