package org.d3if3075.kosku.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "catatan_image",
    foreignKeys = [
        ForeignKey(
            entity = Catatan::class,
            parentColumns = ["id"],
            childColumns = ["catatanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("catatanId")]
)
data class CatatanImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val catatanId: Long,
    val imageUrl: String
)