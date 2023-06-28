package br.com.breno.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.breno.orgs.database.dao.ProductDao
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.utils.Converters

@Database(entities = [Product::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class OrgsDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {

        @Volatile private var db: OrgsDatabase? = null
        fun getInstance(context: Context): OrgsDatabase {
            return db ?: Room.databaseBuilder(
                context = context,
                klass = OrgsDatabase::class.java,
                name = "orgs.db"
            ).build()
                .also {
                    db = it
                }
        }
    }
}