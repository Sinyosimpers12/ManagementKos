package org.d3if3075.kosku.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catatan")
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val catatan: String,
    val nomorkamar: Int,
    val tanggal: String,
    val jenisKelamin: String,
    val tanggalKeluar: String,
    val isDeleted: Boolean = false
)
