package org.d3if3075.kosku.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kos")
data class Kos(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomNumber: String,
    val tenantName: String
)
