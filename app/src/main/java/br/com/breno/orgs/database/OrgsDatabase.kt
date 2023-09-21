package br.com.breno.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.breno.orgs.database.dao.ProductDao
import br.com.breno.orgs.database.dao.UserDao
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.model.User
import br.com.breno.orgs.utils.Converters

@Database(entities = [Product::class, User::class], version = 3, exportSchema = true)
@TypeConverters(Converters::class)
abstract class OrgsDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    abstract fun userDao(): UserDao

    companion object {

        @Volatile private var db: OrgsDatabase? = null
        fun getInstance(context: Context): OrgsDatabase {
            return db ?: Room.databaseBuilder(
                context = context,
                klass = OrgsDatabase::class.java,
                name = "orgs.db"
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                )
                .build()
                .also {
                    db = it
                }
        }
    }
}