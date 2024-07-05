package com.okebenoithub.android.www.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import javax.inject.Inject

/**
 * PdfUtil class
 * This class is used to manage pdf file
 */
class PdfUtil @Inject constructor(val context: Context) {

    /**
     * Launch pdf viewer activity
     * @param pdfUrl :: pdf url
     * @param pdfTitle :: pdf title
     * @param enableDownload :: enable download
     */
    fun openPdfViewerFromUrl(pdfUrl: String, pdfTitle: String, enableDownload: Boolean) {
        PdfViewerActivity.launchPdfFromUrl(
            context = context,
            pdfUrl = pdfUrl,
            pdfTitle = pdfTitle,
            saveTo = saveTo.ASK_EVERYTIME,
            enableDownload = enableDownload
        )
    }

    /**
     * Launch pdf viewer activity
     * @param pdfPath :: pdf path
     * @param pdfTitle :: pdf title
     * @param enableDownload :: enable download
     */
    fun openPdfViewerFromLocalPath(pdfPath: String, pdfTitle: String, enableDownload: Boolean, isFromAssets: Boolean) {
        PdfViewerActivity.launchPdfFromPath(
            context = context,
            path = pdfPath,
            pdfTitle = pdfTitle,
            saveTo = saveTo.ASK_EVERYTIME,
            fromAssets = isFromAssets
        )
    }

    /**
     * Load pdf into view
     * @param pdfRendererView :: pdf renderer view
     * @param lifecycleCoroutineScope :: lifecycle coroutine scope
     * @param lifecycle :: lifecycle
     */
    fun loadPdfIntoView(pdfRendererView: PdfRendererView, pdfUrl: String, lifecycleCoroutineScope: LifecycleCoroutineScope, lifecycle: Lifecycle) {
        pdfRendererView.initWithUrl(
            url = pdfUrl,
            lifecycleCoroutineScope = lifecycleCoroutineScope,
            lifecycle = lifecycle
        )
    }
}