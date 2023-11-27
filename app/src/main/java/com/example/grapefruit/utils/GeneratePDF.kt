package com.example.grapefruit.utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.grapefruit.model.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.ceil

private const val QR_CODE_WIDTH = 400
private const val QR_CODE_HEIGHT = 400
private const val PAGE_WIDTH = 1120
private const val PAGE_HEIGHT = 792
private const val HEADER_HEIGHT = 100
private const val TITLE_TEXT_SIZE = 40F
private const val INFO_TEXT_SIZE = 15F
private const val LEFT_MARGIN = 56F
private const val RIGHT_MARGIN = 656F
private const val INFO_X_OFFSET_ODD = 170F
private const val INFO_X_OFFSET_EVEN = 770F

fun generatePdf(userList:List<User>, title:String, fileName:String){
    try {
        val bitMaps = generateQRCodes(userList)
        val document = PdfDocument()
        val rowCount = ceil(userList.size.toDouble() / 2).toInt()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, rowCount * PAGE_HEIGHT + HEADER_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        drawHeader(canvas, title)
        drawQRCodeAndInfo(canvas,bitMaps, userList)
        document.finishPage(page)
        savePdfDocument(document,fileName)
    } catch (e: WriterException) {
        e.printStackTrace();
    }
}

fun generateQRCodes(userList: List<User>): List<Bitmap> {
    val mWriter = MultiFormatWriter()
    val mEncoder = BarcodeEncoder()
    val bitMaps = mutableListOf<Bitmap>()
    userList.forEach {
        val value = "${it.name}; ${it.share}"
        val mMatrix = mWriter.encode(value, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT)
        val bitMap = mEncoder.createBitmap(mMatrix)
        bitMaps.add(bitMap)
    }
    return bitMaps
}


fun drawHeader(canvas: Canvas, headerTitle: String) {
    val title = Paint().apply {
        typeface = Typeface.DEFAULT
        textSize = TITLE_TEXT_SIZE
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(headerTitle, PAGE_WIDTH / 2F, HEADER_HEIGHT / 2F, title)
}

fun drawQRCodeAndInfo(canvas: Canvas, bitMaps: List<Bitmap>, userList: List<User>) {
    val paint = Paint()
    val info = Paint().apply {
        typeface = Typeface.DEFAULT
        textSize = INFO_TEXT_SIZE
    }
    var top:Float = HEADER_HEIGHT.toFloat()
    bitMaps.forEachIndexed { index, it ->
        val label = "Név: ${userList[index].name}; Hányad: ${userList[index].share}%"
        val xPos = if (index % 2 == 0) LEFT_MARGIN else RIGHT_MARGIN
        val textXPost = if (index % 2 == 0) INFO_X_OFFSET_ODD else INFO_X_OFFSET_EVEN
        canvas.drawBitmap(it, xPos, top, paint)
        top += QR_CODE_HEIGHT
        canvas.drawText(label, textXPost, top, info)
        if (index % 2 == 0) top -= QR_CODE_HEIGHT
    }
}

fun savePdfDocument( document: PdfDocument, fileName: String) {
    val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(folder, "${fileName}.pdf")
    try {
        document.writeTo(FileOutputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        document.close()
    }
}