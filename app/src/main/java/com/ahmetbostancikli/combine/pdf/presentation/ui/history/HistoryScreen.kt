package com.ahmetbostancikli.combine.pdf.presentation.ui.history


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.ahmetbostancikli.combine.pdf.R
import com.ahmetbostancikli.combine.pdf.extension.getFileNameFromUri
import com.ahmetbostancikli.combine.pdf.extension.getPdfPageCount
import com.ahmetbostancikli.combine.pdf.presentation.ui.advertise.Advertise
import java.io.File




//History Screen List Composable Function
@Composable
fun HistoryScreenList(
    modifier: Modifier = Modifier,
    addPDF: () -> Unit,
    pdfList: List<Uri>
) {

    val context = LocalContext.current
    val annotatedTopBarText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color(0xFF1565C0),
                fontSize = 58.24.sp,
                fontFamily = FontFamily(Font(R.font.abril)),
                fontWeight = FontWeight(400)
            )
        ) {
            append(stringResource(id = R.string.pdf))
        }
        withStyle(
            style = SpanStyle(
                color = Color(0xFF1565C0),
                fontSize = 24.sp,
                fontWeight = FontWeight(300)
            )
        ) {
            append(stringResource(id = R.string.merge))
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFD8E4F3))
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(size = 0.dp)
                    ),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = annotatedTopBarText,
                    modifier = modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 0.dp)
                )

                Box {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add_medium),
                        contentDescription = stringResource(
                            id = R.string.add_button_content_description
                        ),
                        contentScale = ContentScale.None,
                        modifier = modifier
                            .padding(top = 32.dp, end = 16.dp)
                            .clickable(onClick = addPDF)
                            .wrapContentSize()
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Advertise().BannerAdView()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (pdfList.isEmpty()) stringResource(id = R.string.not_found)  else "History",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1565C0),
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }



        items(pdfList) {   pdfUri ->
            HistoryScreenItem(
                modifier = Modifier
                    .clickable { openPdf(pdfUri, context) }
                    .padding(start = 8.dp, end = 8.dp),
                title = fileNameExtension(pdfUri, LocalContext.current) ,
                pageCount = pdfPageCount(pdfUri, LocalContext.current)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}






private fun listAllPdfFilesInDirectory(context: Context): List<Uri> {
    val pdfUriList = mutableListOf<Uri>()
    val directory = createPdfMergeDirectory(context)

    if (directory.exists() && directory.isDirectory) {
        val files = directory.listFiles()

        for (file in files) {
            if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                val pdfUri = Uri.fromFile(file)
                pdfUriList.add(pdfUri)
            }
        }
    }

    return pdfUriList
}

private fun createPdfMergeDirectory(context: Context): File {
    val directory = File(context.getExternalFilesDir(null), "PDF-MERGE")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    return directory
}


//History Screen Composable Function
@Composable
fun HistoryScreen(addPDF: () -> Unit) {
    val context = LocalContext.current
    val pdfList =  listAllPdfFilesInDirectory(context)
    HistoryScreenList(addPDF = addPDF,pdfList = pdfList)
}



//This function opens Pdf
private fun openPdf(pdfUri: Uri, context: Context) {
    val file = File(pdfUri.path)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val uri = FileProvider.getUriForFile(context,context.packageName + ".provider",file)
    intent.setDataAndType(uri, "application/pdf")
    if(intent.resolveActivity(context.packageManager) != null){
        context.startActivity(intent)
    }else{
        Toast.makeText(context, "PDF okuyucu bulunamadı.", Toast.LENGTH_LONG).show()
    }
}

//This function counts the  Pdf Page numbers
private fun pdfPageCount(uri: Uri,context: Context): Int {
    return  uri.getPdfPageCount(context)
}


//This function is for file name extension
private fun fileNameExtension(uri: Uri, context: Context):String {
    return uri.getFileNameFromUri(context)
}

//History Screen List Preview
@Preview
@Composable
fun HistoryScreenListPreview() {
    MaterialTheme {
        HistoryScreenList(addPDF = {},pdfList = emptyList())
    }
}



//History Screen Item Preview
@Preview
@Composable
fun HistoryScreenItemPreview() {
    MaterialTheme {
        HistoryScreenItem(Modifier.padding(start = 8.dp, end = 8.dp),stringResource(id = R.string.brief_task),11)
    }
}



//History Screen Item Composable Function
@Composable
fun HistoryScreenItem(
    modifier: Modifier = Modifier,
    title: String,
    pageCount: Int
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.pdf_icon_small),
                contentDescription = stringResource(
                    id = R.string.pdf_icon_content_description
                ),
                modifier = modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF1565C0),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .width(50.dp)
                    .height(50.dp),
                contentScale = ContentScale.None
            )
            Column(
                modifier = modifier
                    .padding(start = 5.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1565C0),
                    ),
                    modifier = modifier.height(17.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(id = R.string.eleven_pages_placeholder, pageCount),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF1565C0),
                    ),
                    modifier = modifier.height(17.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenOfPreview() {
    MaterialTheme {
        HistoryScreen(addPDF = {})
    }
}
