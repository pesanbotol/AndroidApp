package com.pesanbotol.android.app.data.search.viewmodel

import androidx.lifecycle.ViewModel
import com.pesanbotol.android.app.data.search.repository.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    fun search(q: String, searchKind: String? = "all") = searchRepository.search(q, searchKind)
    fun searchBottles(q: String, searchKind: String? = "bottles") = searchRepository.searchBottles(q, searchKind)
//    fun searchUsers(q: String, searchKind: String? = "users") = searchRepository.searchUsers(q, searchKind)
//    fun searchMissions(q: String, searchKind: String? = "missions") = searchRepository.searchMissions(q, searchKind)
}