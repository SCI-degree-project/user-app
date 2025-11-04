package com.userapp.view.components.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.userapp.R
import com.userapp.view.screen.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            iconRes = R.drawable.rounded_home_28,
            selectedIconRes = R.drawable.baseline_home_filled_28,
            description = "Home"
        ),
        BottomNavItem(
            route = Screen.Search.route,
            iconRes = R.drawable.outline_search_28,
            selectedIconRes = R.drawable.bold_outline_search_28,
            description = "Search"
        ),
        BottomNavItem(
            route = Screen.Favorites.route,
            iconRes = R.drawable.rounded_bookmark_28,
            selectedIconRes = R.drawable.baseline_bookmark_28,
            description = "Saved"
        )
    )

    NavigationBar (
        modifier = Modifier.height(60.dp),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val selected = item.route == currentRoute
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) item.selectedIconRes else item.iconRes),
                        contentDescription = item.description
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
            )

        }
    }
}
