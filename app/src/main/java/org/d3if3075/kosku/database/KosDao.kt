package org.d3if3075.kosku.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.d3if3075.kosku.model.Kos

@Dao
interface KosDao {
    @Query("SELECT * FROM kos")
    suspend fun getAllKos(): List<Kos>

    @Insert
    suspend fun insertKos(kos: Kos)

    @Query("SELECT * FROM kos WHERE roomNumber = :roomNumber")
    fun getKosByRoomNumber(roomNumber: String): Kos?

    @Query("SELECT * FROM kos WHERE roomNumber = :roomNumber")
    suspend fun isRoomNumberExists(roomNumber: String): Kos?

    @Update
    suspend fun edit(kos: Kos)

    @Delete
    suspend fun deleteKos(kos: Kos)
}
