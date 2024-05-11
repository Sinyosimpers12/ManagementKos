package org.d3if3075.kosku.database

import org.d3if3075.kosku.model.Kos

class KosRepository(private val kosDao: KosDao) {

    suspend fun getAllKos(): List<Kos> {
        return kosDao.getAllKos()
    }

    suspend fun insertKos(kos: Kos) {
        kosDao.insertKos(kos)
    }

    suspend fun edit(kos: Kos) {
        kosDao.edit(kos)
    }

    suspend fun deleteKos(kos: Kos) {
        kosDao.deleteKos(kos)
    }

    fun getKosByRoomNumber(roomNumber: String): Kos? {
        return kosDao.getKosByRoomNumber(roomNumber)
    }

    suspend fun isRoomNumberExists(roomNumber: String): Boolean {
        val kos = kosDao.isRoomNumberExists(roomNumber)
        return kos != null
    }
}
