package com.ahmetbostancikli.combine.pdf.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmetbostancikli.combine.pdf.R
import com.ahmetbostancikli.combine.pdf.presentation.navigation.BottomNavigationBar
import com.ahmetbostancikli.combine.pdf.presentation.navigation.Navigation
import com.ahmetbostancikli.combine.pdf.presentation.navigation.NavigationItem


@Preview
@Composable
fun PdfAppPreview() {
    val navController = rememberNavController()
    val onNavItemClicked: (NavigationItem) -> Unit = { }
    MaterialTheme {
        PdfApp(
            addPDF = {

            },
            navController,
            onNavItemClicked,
            0,
            false
        )
    }
}


//Pdf App Composable Function
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PdfApp(
    addPDF: () -> Unit,
    navController: NavHostController,
    onNavItemClicked: (NavigationItem) -> Unit,
    listSize: Int,
    isLoading: Boolean
) {


    //Scaffold is taking place the whole screen parts in it, Such as Bottom bar, background, Navigation bar, activities, fragments and etc.
    Scaffold(

        bottomBar = { BottomNavigationBar(navController, onNavItemClicked, listSize = listSize) },

        backgroundColor = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    color = colorResource(id = R.color.pdf_document_text_color)
                )
            } else {
                Navigation(navController = navController, addPdf = addPDF)
            }
        }
    }

}