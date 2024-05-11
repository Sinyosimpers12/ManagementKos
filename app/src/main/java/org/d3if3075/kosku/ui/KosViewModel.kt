package org.d3if3075.kosku.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.d3if3075.kosku.database.KosRepository
import org.d3if3075.kosku.model.Kos

class KosViewModel(private val repository: KosRepository) : ViewModel() {
    private val _kosList = MutableStateFlow<List<Kos>>(emptyList())
    val kosList: StateFlow<List<Kos>> = _kosList

    private val _deletedKos = MutableStateFlow<Kos?>(null)
    val deletedKos: StateFlow<Kos?> = _deletedKos



    fun getAllKos() {
        viewModelScope.launch {
            _kosList.value = repository.getAllKos()
        }
    }

    fun insertKos(kos: Kos) {
        viewModelScope.launch {
            repository.insertKos(kos)
            getAllKos()
        }
    }

    fun edit(kos: Kos) {
        viewModelScope.launch {
            repository.edit(kos)
        }
    }


    fun deleteKos(kos: Kos) {
        viewModelScope.launch {
            repository.deleteKos(kos)
            getAllKos()
            // Update deletedKos after deletion
            _deletedKos.value = kos
        }
    }

    suspend fun isRoomNumberExists(roomNumber: String): Boolean {
        return repository.isRoomNumberExists(roomNumber)
    }

    fun clearDeletedKos() {
        _deletedKos.value = null
    }



    fun init(kosRepository: KosRepository) {
        viewModelScope.launch {
            _kosList.value = kosRepository.getAllKos()
        }
    }

}
