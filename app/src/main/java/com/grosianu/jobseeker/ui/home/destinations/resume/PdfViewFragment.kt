package com.grosianu.jobseeker.ui.home.destinations.resume

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentPdfWebViewBinding
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class PdfViewFragment : Fragment() {

    private var _binding: FragmentPdfWebViewBinding? = null
    private val binding get() = _binding!!

    private val args: PdfViewFragmentArgs by navArgs()
    private lateinit var url: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            url = URLEncoder.encode(args.pdfUrl, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        showPdfFile(url)
    }

    private fun showPdfFile(pdfUrl: String) {
        showProgress()
        binding.apply {
            pdfView.invalidate()
            pdfView.settings.javaScriptEnabled = true
            pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=$pdfUrl")
            pdfView.webViewClient = object : WebViewClient() {
                var checkOnPageStartedCalled = false
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    checkOnPageStartedCalled = true
                    hideProgress()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (checkOnPageStartedCalled) {
                        //hideProgress()
                    } else {
                        showPdfFile(pdfUrl)
                    }
                }
            }
        }
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }
}