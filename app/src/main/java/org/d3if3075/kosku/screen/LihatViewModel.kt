package org.d3if3075.kosku.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3075.kosku.database.CatatanRepository

class LihatViewModel(private val repository: CatatanRepository) : ViewModel() {

    val catatanList = repository.getAllCatatan() // assuming this method exists

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAsDeleted(id)
        }
    }
}
