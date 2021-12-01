package jp.microvent.microvent.service

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*


class AppWebViewClient(
    val callback: AppWebViewClientCallBack
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        callback.onPageStarted()
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)

        callback.onPageCommitVisible()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        callback.onReceivedError()
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)

        callback.onReceivedHttpError()
    }

    //TODO:DELETE ME
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed()
    }
}

interface AppWebViewClientCallBack {
    fun onPageStarted()
    fun onPageCommitVisible()
    fun onReceivedError()
    fun onReceivedHttpError()
}