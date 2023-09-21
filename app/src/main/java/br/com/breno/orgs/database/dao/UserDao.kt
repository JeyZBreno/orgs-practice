package br.com.breno.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.breno.orgs.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun saveUser(user: User)

    @Query("""
        SELECT * FROM user
        WHERE id = :id
        AND password = :password
    """)
    suspend fun authUser(
        id: String,
        password:String,
    ): User?

    @Query("SELECT * FROM user Where id = :id")
    fun findById(id: String): Flow<User>

    @Query("SELECT * FROM User")
    fun findAll(): Flow<List<User>>
}