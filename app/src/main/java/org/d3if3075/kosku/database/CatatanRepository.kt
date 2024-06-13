package org.d3if3075.kosku.database

import kotlinx.coroutines.flow.Flow
import org.d3if3075.kosku.model.Catatan

class CatatanRepository(private val dao: CatatanDao) {

    fun getAllCatatan(): Flow<List<Catatan>> {
        return dao.getAllCatatan()
    }

    suspend fun markAsDeleted(id: Long) {
        dao.markAsDeleted(id)
    }


}
