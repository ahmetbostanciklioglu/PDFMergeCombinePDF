package com.ahmetbostancikli.combine.pdf

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmetbostancikli.combine.pdf.databinding.ActivityMainBinding

import com.ahmetbostancikli.combine.pdf.presentation.PdfApp
import com.ahmetbostancikli.combine.pdf.presentation.base.PdfPickerActivity
import com.ahmetbostancikli.combine.pdf.presentation.navigation.NavigationItem
import com.ahmetbostancikli.combine.pdf.presentation.ui.advertise.Advertise
import com.ahmetbostancikli.combine.pdf.utils.PdfMergeTool
import com.google.android.gms.ads.MobileAds
import com.wada811.databindingktx.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : PdfPickerActivity(R.layout.activity_main) {


    //Injecting Pdf Merge Tool
    @Inject
    lateinit var pdfMergeTool: PdfMergeTool



    private var newSize by mutableStateOf(0)
    private var isLoading by mutableStateOf(false)

    //Advertise Variable
    private lateinit var advertise: Advertise

    private val binding: ActivityMainBinding by dataBinding()
    private lateinit var navController: NavHostController
    private var uriList: MutableList<Uri> = mutableListOf()






    @Composable
    fun PdfAppContent() {
        navController = rememberNavController()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContent {
            //TODO: Updated:
            navController = rememberNavController()

            PdfAppContent()

            PdfApp(
                addPDF = {
                    openFilePicker()
                },

                navController,

                onNavItemClicked,

                newSize,

                isLoading
            )
        }
    }


    private val onNavItemClicked: (NavigationItem) -> Unit = { item ->

        when (item) {

            //Navigation Item is Home
            is NavigationItem.Home -> {

            }

            //Navigation Item is Merge
            is NavigationItem.Merge -> if(uriList.size>1) {

                isLoading = true
                pdfMergeTool.mergePDFs(uriList)

            }

            //Navigation Item is History
            is NavigationItem.History -> {

            }
        }
    }


    //Main Screen Of Preview Function
    @Preview
    @Composable
    fun MainScreenOfPreview() {
        MaterialTheme {
            PdfAppContent()
            PdfApp(
                addPDF = {
                    openFilePicker()
                },
                navController,
                onNavItemClicked,
                newSize,
                isLoading
            )
        }
    }


    private fun initListener() = with(binding) {


        MobileAds.initialize(this@MainActivity) {}


        advertise = Advertise(this@MainActivity)

        advertise.showInterstitialAd()

        setSuccessListener { _, uris ->

            uriList = uris

            newSize = uris.size

            navController.navigate("${NavigationItem.Merge.route}?pdfList=$uris")
        }

        pdfMergeTool.setSuccessListener { isSuccess, errorMessage ->


            if (isSuccess) {

                Toast.makeText(
                    this@MainActivity,
                    "Download dizinine kayıt edilerek birleştirme tamamlandı",
                    Toast.LENGTH_SHORT
                ).show()

                isLoading = false

                navController.navigate(NavigationItem.History.route)

                advertise.showRewardedAd()

            } else {
                Toast.makeText(this@MainActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()

            }
            uriList.clear()

        }

    }

}



