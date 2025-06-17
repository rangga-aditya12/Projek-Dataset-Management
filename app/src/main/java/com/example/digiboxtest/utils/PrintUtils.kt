package com.example.digiboxtest.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.example.digiboxtest.database.DatasetEntity
import java.io.FileOutputStream
import java.io.IOException

fun printDatasetDetails(context: Context, dataset: DatasetEntity) {

    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
    val jobName = "Dataset_Details_${dataset.title}"

    printManager.print(jobName, object : PrintDocumentAdapter() {
        private var document: PdfDocument? = null

        override fun onLayout(
            oldAttributes: PrintAttributes,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal,
            callback: LayoutResultCallback,
            extras: Bundle?
        ) {
            document = PdfDocument()
            if (cancellationSignal.isCanceled) {
                callback.onLayoutCancelled()
                return
            }
            val info = PrintDocumentInfo.Builder(jobName)
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1) // Hanya satu halaman
                .build()
            callback.onLayoutFinished(info, true)
        }

        override fun onWrite(
            pages: Array<out PageRange>,
            destination: ParcelFileDescriptor,
            cancellationSignal: CancellationSignal,
            callback: WriteResultCallback
        ) {
            val page = document?.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create()) // Ukuran A4
            if (cancellationSignal.isCanceled) {
                callback.onWriteCancelled()
                document?.close()
                document = null
                return
            }
            drawPage(page, dataset)
            document?.finishPage(page)

            try {
                FileOutputStream(destination.fileDescriptor).use {
                    document?.writeTo(it)
                }
            } catch (e: IOException) {
                callback.onWriteFailed(e.toString())
                return
            } finally {
                document?.close()
                document = null
            }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        }

        private fun drawPage(page: PdfDocument.Page?, dataset: DatasetEntity) {
            val canvas = page?.canvas ?: return
            val titlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 20f
                isFakeBoldText = true
            }
            val bodyPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 14f
            }

            var yPosition = 60f
            val xPosition = 40f

            canvas.drawText("Dataset Details: ${dataset.title}", xPosition, yPosition, titlePaint)
            yPosition += 40f

            canvas.drawText("Description: ${dataset.description}", xPosition, yPosition, bodyPaint)
            yPosition += 25f
            canvas.drawText("Category: ${dataset.category}", xPosition, yPosition, bodyPaint)
            yPosition += 25f
            canvas.drawText("Creator: ${dataset.creator}", xPosition, yPosition, bodyPaint)
            yPosition += 25f
            canvas.drawText("Verifier: ${dataset.verifier}", xPosition, yPosition, bodyPaint)
            yPosition += 25f
            canvas.drawText("Keywords: ${dataset.keywords}", xPosition, yPosition, bodyPaint)
            yPosition += 40f
            canvas.drawText("Contains ${dataset.rowCount} rows and ${dataset.featureCount} features.", xPosition, yPosition, bodyPaint)
        }
    }, null)
}