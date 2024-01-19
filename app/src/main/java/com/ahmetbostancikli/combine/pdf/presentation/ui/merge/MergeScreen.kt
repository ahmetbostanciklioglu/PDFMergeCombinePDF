package com.ahmetbostancikli.combine.pdf.presentation.ui.merge


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.ahmetbostancikli.combine.pdf.R
import com.ahmetbostancikli.combine.pdf.extension.getFileNameFromUri
import com.ahmetbostancikli.combine.pdf.extension.getPdfPageCount


//Merge Screen List Composable Function
@Composable
fun MergeScreenList(
    modifier: Modifier = Modifier,
    addPDF: () -> Unit,
    pdfList: List<Uri>
) {

    //Context
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

    //Merge File Number
    val mergeFileNumber = buildAnnotatedString {

        //Span Style of first part
        withStyle(
            style = SpanStyle(
                color = Color(0xFF1565C0),
                fontSize = 18.sp,

                )
        ) {

            append(stringResource(id = R.string.three, pdfList.size))
        }

        //Span Style of second part
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 18.sp
            )
        ) {

            append(stringResource(id = R.string.files_will_be_merged))
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
                    .height(134.dp)
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
                        .padding(start = 16.dp, top = 45.dp)
                )

                Box {

                    Image(
                        painter = painterResource(id = R.drawable.ic_add_medium),
                        contentDescription = stringResource(
                            id = R.string.add_button_content_description
                        ),
                        modifier = modifier
                            .padding(top = 86.dp, end = 16.dp)
                            .clickable(onClick = addPDF)
                            .wrapContentSize(),
                        contentScale = ContentScale.None,

                        )
                }
            }
        }


        item {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mergeFileNumber,
                modifier = modifier
                    .padding(start = 16.dp),
                fontWeight = FontWeight(600),
                fontFamily = FontFamily(Font(R.font.inter)),

                )

            Spacer(modifier = Modifier.height(16.dp))
        }


        //İtems for PdfUri
        items(pdfList) { pdfUri ->
            MergeScreenItem(
                modifier = Modifier
                    .clickable { openPdf(pdfUri, context) }
                    .padding(start = 8.dp, end = 8.dp),
                title = fileNameExtension(pdfUri, LocalContext.current),
                pageCount = pdfPageCount(pdfUri, LocalContext.current)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


//Open Pdf
private fun openPdf(pdfUri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(pdfUri, "application/pdf")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "PDF okuyucu bulunamadı.", Toast.LENGTH_LONG).show()
    }
}


//File Name Extension
private fun fileNameExtension(uri: Uri, context: Context): String {
    return uri.getFileNameFromUri(context)
}


//Pdf Page Count


private fun pdfPageCount(uri: Uri, context: Context): Int {
    return uri.getPdfPageCount(context)

}


@Composable
fun MergeScreenItem(
    title: String,
    pageCount: Int,
    modifier: Modifier = Modifier,

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
                    modifier = modifier.height(17.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1565C0),
                    ),

                    )

                Spacer(modifier = Modifier.height(3.dp))


                Text(
                    text = stringResource(id = R.string.eleven_pages_placeholder, pageCount),
                    modifier = modifier.height(17.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF1565C0),
                    ),

                    )
            }
        }
    }
}


//Merge Screen List Composable Of Preview Function
@Preview
@Composable
fun MergeScreenListPreview() {
    MaterialTheme {
        MergeScreenList(addPDF = {}, pdfList = emptyList())
    }
}


//Merge Screen Item Composable Of Preview Function
@Preview
@Composable
fun MergeScreenItemPreview() {
    MaterialTheme {
        MergeScreenItem(
            stringResource(id = R.string.brief_task),
            11,
            Modifier.padding(start = 8.dp, end = 8.dp),
        )
    }
}


//Merge Screen Composable Of Preview Function
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MergeScreenOfPreview() {
    MaterialTheme {
        MergeScreen(addPDF = {}, pdfList = emptyList())
    }
}


//Merge Screen Composable Function
@Composable
fun MergeScreen(addPDF: () -> Unit, pdfList: List<Uri>) {
    MergeScreenList(addPDF = addPDF, pdfList = pdfList)
}
