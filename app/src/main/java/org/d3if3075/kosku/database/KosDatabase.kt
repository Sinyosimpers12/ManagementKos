package org.d3if3075.kosku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3075.kosku.model.Kos

@Database(entities = [Kos::class], version = 1, exportSchema = false)
abstract class KosDatabase : RoomDatabase() {

    abstract fun kosDao(): KosDao

    companion object {
        @Volatile
        private var INSTANCE: KosDatabase? = null

        fun getDatabase(context: Context): KosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KosDatabase::class.java,
                    "kos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
