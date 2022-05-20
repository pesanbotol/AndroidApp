package com.pesanbotol.android.app.data.search.viewmodel

import androidx.lifecycle.ViewModel
import com.pesanbotol.android.app.data.search.repository.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    fun search(q: String, searchKind: String? = "all") = searchRepository.search(q, searchKind)
}