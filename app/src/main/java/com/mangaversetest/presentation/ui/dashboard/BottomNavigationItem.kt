package com.mangaversetest.presentation.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.mangaversetest.navigation.DashBoardNavigationScreens

data class BottomNavigationItem(
    val title: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val screenRoute: String = ""
)

fun getBottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Home",
            icon = Icons.Filled.Home,
            screenRoute = DashBoardNavigationScreens.Manga.route
        ),
        BottomNavigationItem(
            title = "Face",
            icon = Icons.Filled.Face,
            screenRoute = DashBoardNavigationScreens.Face.route
        )
    )
}
