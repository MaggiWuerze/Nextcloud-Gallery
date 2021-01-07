package de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder

import androidx.room.*
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder


@Dao
interface RemoteFolderDao {

    @Update
    suspend fun update(note: RemoteFolder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(p: RemoteFolder): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(p: List<RemoteFolder>): List<Long>

    @Query(value = "SELECT * FROM remotefolder ORDER BY name")
    fun list(): List<RemoteFolder>

    @Query("SELECT * from remotefolder WHERE id = :id")
    fun get(id: Long): RemoteFolder?

    @Query("DELETE FROM remotefolder")
    fun clear()

    @Delete
    suspend fun delete(p: RemoteFolder)

}