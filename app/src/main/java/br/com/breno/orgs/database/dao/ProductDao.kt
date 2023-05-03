package br.com.breno.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.breno.orgs.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun findAll() : List<Product>

    @Insert
    fun save(vararg product: Product)

    @Update
    fun updateItem(vararg product: Product)

    @Delete
    fun deleteItem(vararg product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun findById(id: Long) : Product?
}