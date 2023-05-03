package br.com.breno.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.breno.orgs.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun findAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg product: Product)

    @Delete
    fun deleteItem(vararg product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun findById(id: Long): Product?

    @Query("SELECT * FROM Product ORDER BY name ASC")
    fun findAllOrderedByNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    fun findAllOrderedByNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    fun findAllOrderedByDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    fun findAllOrderedByDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value ASC")
    fun findAllOrderedByValueAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value DESC")
    fun findAllOrderedByValueDesc(): List<Product>
}