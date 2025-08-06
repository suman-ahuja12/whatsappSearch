package com.example.whatsappsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappsearch.api.RetrofitClient
import com.example.whatsappsearch.model.StateDataItem
import kotlinx.coroutines.launch



class StateViewModel : ViewModel() {

    private val _stateList = MutableLiveData<List<StateDataItem>>()
    val stateList: LiveData<List<StateDataItem>> get() = _stateList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadStateData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getStateList()
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()?.Data ?: emptyList()
                    _stateList.value = list

                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
