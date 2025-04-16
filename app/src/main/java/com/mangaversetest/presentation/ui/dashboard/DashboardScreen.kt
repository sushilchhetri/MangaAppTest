package com.mangaversetest.presentation.ui.dashboard

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.mangaversetest.R
import com.mangaversetest.domain.ManageModel
import com.mangaversetest.navigation.DashBoardNavigationScreens
import com.mangaversetest.presentation.ui.dashboard.screens.FaceScreen
import com.mangaversetest.presentation.ui.dashboard.screens.MangaDetailScreen
import com.mangaversetest.presentation.ui.dashboard.screens.MangaListScreen
import com.mangaversetest.presentation.ui.dashboard.viewmodel.FaceViewModel
import com.mangaversetest.presentation.ui.dashboard.viewmodel.MangaViewModel

@Composable
fun DashboardScreen() {
    val ctx = LocalContext.current
    val navController = rememberNavController()
    val mangaVM: MangaViewModel = hiltViewModel()
    val faceVM: FaceViewModel = hiltViewModel()
    var navigationSelectedItem by rememberSaveable { mutableIntStateOf(0) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry.value?.destination?.route) {
        when (currentBackStackEntry.value?.destination?.route) {
            DashBoardNavigationScreens.Manga.route -> navigationSelectedItem = 0
            DashBoardNavigationScreens.Face.route -> navigationSelectedItem = 1
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.black)
            ) {
                getBottomNavigationItems().forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        label = {
                            Text(
                                text = navigationItem.title,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.white)
                            )
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.title,
                                modifier = Modifier.semantics { testTag = navigationItem.title },
                                tint = if (index == navigationSelectedItem) colorResource(id = R.color.black)
                                else colorResource(id = R.color.white)
                            )
                        },
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.screenRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = DashBoardNavigationScreens.Manga.route,
            modifier = Modifier.padding(paddingValues = padding)
        ) {
            composable(DashBoardNavigationScreens.Manga.route) {
                MangaListScreen(mangaVM) { manga ->
                    val encodedJson = Uri.encode(Gson().toJson(manga))
                    navController.navigate(DashBoardNavigationScreens.MangaDetail.createRoute(encodedJson))
                }
            }
            composable(
                route = DashBoardNavigationScreens.MangaDetail.route,
                arguments = listOf(navArgument("mangaJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val mangaJson = backStackEntry.arguments?.getString("mangaJson")
                val manga = Gson().fromJson(mangaJson, ManageModel::class.java)
                MangaDetailScreen(mangaModel = manga)
            }
            composable(DashBoardNavigationScreens.Face.route) {
                FaceScreen(faceVM)
            }
        }

    }
}


