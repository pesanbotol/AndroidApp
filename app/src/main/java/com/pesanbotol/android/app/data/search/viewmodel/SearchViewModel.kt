package com.pesanbotol.android.app.data.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.data.search.model.SearchResult
import com.pesanbotol.android.app.data.search.repository.SearchRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _postSearchState: MutableLiveData<StateHandler<SearchResult>> = MutableLiveData()
    val postSearchState: LiveData<StateHandler<SearchResult>> = _postSearchState
    fun search(q: String) {
        viewModelScope.launch {
            _postSearchState.postValue(StateHandler.Loading())
            try {
                val searchResult = SearchResult(

                )
                searchUsers(q).addOnSuccessListener {
                    searchResult.users = it
                    _postSearchState.postValue(StateHandler.Success(SearchResult(users = it)))
                }
//                val missionResult = searchMissions(q).result
                searchBottles(q).addOnSuccessListener {
                    searchResult.bottles = it
                    _postSearchState.postValue(StateHandler.Success(SearchResult(bottles = it)))
                }

            } catch (e: Exception) {
                _postSearchState.postValue(StateHandler.Error("Error searching : ${e.toString()}"))
            }

        }
    }

    private fun searchBottles(q: String) =
            searchRepository.searchBottles(q)

    fun searchUsers(q: String) =
            searchRepository.searchUsers(q)

    suspend fun searchMissions(q: String, searchKind: String? = "missions") =
            searchRepository.searchMissions(q)
}