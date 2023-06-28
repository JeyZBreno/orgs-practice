package br.com.breno.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.breno.orgs.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun findAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg product: Product)

    @Delete
    suspend fun deleteItem(vararg product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun findById(id: Long): Flow<Product?>

    @Query("SELECT * FROM Product ORDER BY name ASC")
    suspend fun findAllOrderedByNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    suspend fun findAllOrderedByNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    suspend fun findAllOrderedByDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    suspend fun findAllOrderedByDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value ASC")
    suspend fun findAllOrderedByValueAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value DESC")
    suspend fun findAllOrderedByValueDesc(): List<Product>
}