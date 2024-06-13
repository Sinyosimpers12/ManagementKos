package org.d3if3075.kosku.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3075.kosku.model.Catatan
import org.d3if3075.kosku.model.CatatanImage


@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan)

    @Update
    suspend fun update(catatan: Catatan)

    @Query("SELECT * FROM catatan WHERE isDeleted = 0")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM catatan WHERE nomorkamar = :nomorkamar LIMIT 1")
    suspend fun getCatatanByRoomNumber(nomorkamar: Int): Catatan?

    @Insert
    suspend fun insertCatatanImage(catatanImage: CatatanImage)

    @Query("SELECT * FROM catatan_image WHERE catatanId = :catatanId")
    suspend fun getCatatanImages(catatanId: Long): List<CatatanImage>


    @Query("UPDATE catatan SET jenisKelamin = :jenisKelamin WHERE id = :id")
    suspend fun updateJenisKelamin(id: Long, jenisKelamin: String)

    @Query("UPDATE catatan SET tanggalKeluar = :tanggalKeluar WHERE id = :id")
    suspend fun updateTanggalKeluar(id: Long, tanggalKeluar: String)

    @Query("SELECT * FROM catatan WHERE jenisKelamin = :jenisKelamin ORDER BY tanggal DESC")
    fun getCatatanByGender(jenisKelamin: String): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE isDeleted = :isDeleted")
    fun getDeletedCatatans(isDeleted: Boolean): List<Catatan?>?

    @Query("SELECT * FROM catatan WHERE isDeleted = 1")
    fun getAllCatatan(): Flow<List<Catatan>>

    @Query("UPDATE catatan SET isDeleted = 1 WHERE id = :id")
    suspend fun markAsDeleted(id: Long)

}
