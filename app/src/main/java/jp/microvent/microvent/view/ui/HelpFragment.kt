package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import java.lang.Exception
import java.net.ConnectException

private const val BASE_URL = "http://manual.microvent.r102.jp/"

class HelpFragment : BaseFragment(), OnBackKeyPressedListener {

    private val args: HelpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val url = BASE_URL + args.urlPath

        val view = inflater.inflate(R.layout.fragment_help, container, false)

        val webView = view.findViewById<WebView>(R.id.webView)
        webView.apply {
            webViewClient = WebViewClient() // WebViewを設定する

            settings.javaScriptEnabled = true // JavaScriptを有効にする
            try {
                progressBar.visibility = View.VISIBLE
                loadUrl(url) // URLを読み込む
                progressBar.visibility = View.GONE
            } catch (e: ConnectException){
                progressBar.visibility = View.GONE
                val dialog = DialogConnectionErrorFragment()
                dialog.show(requireActivity().supportFragmentManager, "connection_error")
            }
        }

        setupBackButton()
        setHasOptionsMenu(true)

        return view
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
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        parentFragmentManager.popBackStack()
    }
}