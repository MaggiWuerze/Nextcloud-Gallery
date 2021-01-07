package de.maggiwuerze.nextcloudgallery.model.album

import androidx.lifecycle.LiveData
import androidx.room.*
import de.maggiwuerze.nextcloudgallery.model.account.Account


@Dao
interface AccountDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(account: Account)

    @Query("DELETE FROM account")
    fun deleteAllAlbums()

    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE username = :username AND url = :url")
    fun accountByUsernameAndPassword(username: String, url: String): List<Account>

    @Query("SELECT * FROM account WHERE active")
    fun activeAccount(): Account

}