package com.ahmetbostancikli.combine.pdf.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.ahmetbostancikli.combine.pdf.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


//PDF Merge Tool Function
class PdfMergeTool(private val context: Context) {

    //Set success Listener Function
    fun setSuccessListener(successListener: (Boolean, String) -> Unit) {
        this.successListener = successListener
    }


    //Success Listener variable
    private var successListener: ((Boolean, String) -> Unit)? = null




    //Merge PDfs Function
    fun mergePDFs(selectedPdfUriList: MutableList<Uri>) {
        CoroutineScope(Dispatchers.IO).launch {

        if (selectedPdfUriList.isNotEmpty()) {

            try {
                    val pdfDocument = PdfDocument()
                    for (i in selectedPdfUriList.indices) {

                            val parcelFileDescriptor: ParcelFileDescriptor? =
                                context.contentResolver.openFileDescriptor(selectedPdfUriList[i], "r")

                            if (parcelFileDescriptor != null) {

                                val renderer = PdfRenderer(parcelFileDescriptor)
                                val pageCount = renderer.pageCount


                                for (pageIndex in 0 until pageCount) {

                                    val page = renderer.openPage(pageIndex)

                                    val bitmap = Bitmap.createBitmap(
                                        page.width*2,
                                        page.height*2,
                                        Bitmap.Config.ARGB_8888
                                    )


                                    val canvas = Canvas(bitmap)
                                    canvas.drawColor(Color.WHITE)

                                    page.render(
                                        bitmap,
                                        null,
                                        null,
                                        PdfRenderer.Page.RENDER_MODE_FOR_PRINT
                                    )
                                    
                                    val pageInfo = PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create()

                                    val pdfPage = pdfDocument.startPage(pageInfo)

                                    val pdfCanvas = pdfPage.canvas


                                    pdfCanvas.drawBitmap(bitmap, 0f, 0f, null)

                                    pdfDocument.finishPage(pdfPage)

                                    page.close()
                                }
                                renderer.close()
                            }

                    }

                    savePdfDocument(pdfDocument)


            } catch (e: Exception) {
                e.localizedMessage?.let {
                    withContext(Dispatchers.Main) {
                        successListener?.invoke(false, it)
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                successListener?.invoke(false, context.getString(R.string.not_empty))
            }
        }
        }
    }



    //Create PDF Merge Directory Function
    private fun createPdfMergeDirectory(): File {
        val directory = File(context.getExternalFilesDir(null), "PDF-MERGE")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    //Save PDf Document Function
    private fun savePdfDocument(pdfDocument: PdfDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            val fileName = context.getString(R.string.pdfmerge) + SimpleDateFormat(context.getString(R.string.date_format_text), Locale.UK).format(Calendar.getInstance().time)

            val file = File(createPdfMergeDirectory(),"/$fileName.pdf")

            try {
                pdfDocument.writeTo(FileOutputStream(file))
                withContext(Dispatchers.Main) {
                    successListener?.invoke(true,"")
                }
            } catch (e: IOException) {
                e.localizedMessage?.let {
                    withContext(Dispatchers.Main) {
                        successListener?.invoke(false, it)
                    }
                }
            }
            pdfDocument.close()
        }

    }




    //Companion Object

    companion object {
        private const val A4_WIDTH = 595*2
        private const val A4_HEIGHT = 842*2
    }
}