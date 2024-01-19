package com.ahmetbostancikli.combine.pdf.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ahmetbostancikli.combine.pdf.R
import com.ahmetbostancikli.combine.pdf.presentation.ui.history.HistoryScreen
import com.ahmetbostancikli.combine.pdf.presentation.ui.home.HomeScreen
import com.ahmetbostancikli.combine.pdf.presentation.ui.merge.MergeScreen


/*Bottom Navigation Bar*/
@Composable
fun BottomNavigationBar(navController: NavController, onNavItemClicked: (NavigationItem) -> Unit, modifier: Modifier = Modifier, listSize : Int) {


    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Merge,
        NavigationItem.History
    )


    Surface(
        modifier = modifier
            .height(70.dp)
    ) {
        BottomNavigation(
            backgroundColor = Color.White
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                val color = if (listSize >= 2 ) {
                    colorResource(id = R.color.pdf_document_text_color)
                } else {
                    colorResource(id = R.color.unselected_color)
                }
                BottomNavigationItem(
                    icon = {
                        if (item == NavigationItem.Merge) {
                            Row {
                                Icon(
                                    painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(
                                            color = color
                                        )
                                        .size(60.dp)
                                        .padding(all = 18.dp)
                                        .align(Alignment.Top)
                                        .padding(bottom = 4.dp)
                                )

                            }
                        } else {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = null
                            )
                        }
                    },

                    selectedContentColor = if (item == NavigationItem.Merge) {
                        colorResource(id = R.color.white)
                    } else {
                        colorResource(id = R.color.pdf_document_text_color)
                    },
                    unselectedContentColor = if (item == NavigationItem.Merge) {
                        colorResource(id = R.color.white)
                    } else {
                        colorResource(id = R.color.unselected_color)
                    },
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        onNavItemClicked(item)
                        navController.navigate(item.route) {

                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true

                        }

                    }
                )
            }
        }
    }

}



//Navigation for Android Project

@Composable
fun Navigation(navController: NavHostController, addPdf: () -> Unit) {

    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(addPDF = addPdf)
        }
        composable("${NavigationItem.Merge.route}?pdfList={pdfList}") {
            val uriListString = it.arguments?.getString("pdfList")
            val pdfList: List<Uri>? = if (uriListString != null) {
                val uriList = uriListString.split(',').map {
                    Uri.parse(it.trim('[', ']', ' ', '\n', '\t')) }
                uriList
            } else {
                null
            }

            pdfList?.let { pdfList ->
                MergeScreen(addPDF = addPdf, pdfList = pdfList.toList())
            } ?: HomeScreen(addPDF = addPdf)
        }
        composable(NavigationItem.History.route) {
            HistoryScreen(addPDF = addPdf)
        }
    }
}




//Sealed class For NavigationItem which are Home, Merge and History screens.


sealed class NavigationItem(var route: String, var icon: Int) {
    data object Home : NavigationItem("home", R.drawable.home_icon)
    data object Merge : NavigationItem("merge", R.drawable.merge_arrow)
    data object History : NavigationItem("history", R.drawable.history_icon)
}
