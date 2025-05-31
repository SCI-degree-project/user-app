package com.userapp.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.database.dao.SearchHistoryDao
import com.userapp.database.entities.SearchHistoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val dao: SearchHistoryDao
) : ViewModel() {

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistoryEntity>> = _searchHistory

    init {
        viewModelScope.launch {
            dao.getRecentSearches().collect {
                _searchHistory.value = it
            }
        }
    }

    fun saveQuery(query: String) {
        viewModelScope.launch {
            dao.insertQuery(SearchHistoryEntity(query = query))
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dao.clearSearchHistory()
        }
    }

    fun deleteHistoryItem(id: String) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }
}
