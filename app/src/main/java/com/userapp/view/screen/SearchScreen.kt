package com.userapp.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.userapp.R
import com.userapp.view.components.BottomNavigationBar
import com.userapp.view.components.FilterDialog
import com.userapp.view.components.SearchBar
import com.userapp.view.components.StaggeredProductGrid
import com.userapp.viewmodel.search.SearchHistoryViewModel
import com.userapp.viewmodel.search.SearchViewModel
import com.userapp.viewmodel.uistate.UiState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    historyViewModel: SearchHistoryViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val searchResults by viewModel.searchResults.collectAsState()
    var lastSubmittedQuery by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val history by historyViewModel.searchHistory.collectAsState()

    var sortBy by remember { mutableStateOf("name") }
    var sortDirection by remember { mutableStateOf("asc") }
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                SearchBar(
                    query = query,
                    onQueryChange = {
                        query = it
                    },
                    onSearch = {
                        isEditing = false
                        if (query.text.length >= 2) {
                            lastSubmittedQuery = query.text
                            viewModel.searchProducts(query.text, sortBy, sortDirection)
                            historyViewModel.saveQuery(query.text)
                        }
                    },
                    onFocus = { isEditing = true },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { showFilterDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_tune_24),
                        contentDescription = "Filters"
                    )
                }
            }

            if (showFilterDialog) {
                FilterDialog(
                    currentSortBy = sortBy,
                    currentDirection = sortDirection,
                    onDismiss = { showFilterDialog = false },
                    onApply = { newSortBy, newDirection ->
                        sortBy = newSortBy
                        sortDirection = newDirection
                        viewModel.searchProducts(query.text, newSortBy, newDirection)
                        showFilterDialog = false
                    }
                )
            }

            if (history.isNotEmpty() && isEditing) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Recent Searches",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column {
                    history.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = "History",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Text(
                                text = item.query,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        query = TextFieldValue(item.query)
                                        lastSubmittedQuery = item.query
                                        viewModel.searchProducts(item.query, sortBy, sortDirection)
                                        historyViewModel.saveQuery(item.query)
                                        isEditing = false
                                    }
                            )

                            IconButton(
                                onClick = { historyViewModel.deleteHistoryItem(item.query) }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_close_24),
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = searchResults) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }

                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No results found")
                        }
                    } else {
                        StaggeredProductGrid(
                            products = state.data,
                            modifier = modifier,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        }
    }
}
