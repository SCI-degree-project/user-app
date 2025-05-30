package com.userapp.view.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.userapp.R
import com.userapp.view.screen.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.rounded_home_24), contentDescription = "Home") },
            selected = false,
            onClick = { navController.navigate(Screen.Catalog.route) }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_search_24), contentDescription = "Search") },
            selected = false,
            onClick = { navController.navigate(Screen.Search.route) }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_star_border_24), contentDescription = "Favorites") },
            selected = false,
            onClick = { navController.navigate(Screen.Favorites.route) }
        )
    }
}
