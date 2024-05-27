package org.d3if3075.kosku.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3075.kosku.model.Catatan

@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan)

    @Update
    suspend fun update(catatan: Catatan)

    @Query("SELECT * FROM catatan ORDER BY tanggal DESC")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?


    @Query("SELECT * FROM catatan WHERE nomorkamar = nomorkamar")
    suspend fun getCatatanByNomorKamar(id: Long): Catatan?

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

}