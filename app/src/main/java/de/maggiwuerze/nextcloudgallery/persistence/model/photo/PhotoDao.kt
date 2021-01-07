package de.maggiwuerze.nextcloudgallery.persistence.model.photo

import androidx.lifecycle.LiveData
import androidx.room.*
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo


@Dao
interface PhotoDao {

    @Update
    suspend fun update(note: Photo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(p: Photo): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(p: List<Photo>): List<Long>

    @Query(value = "SELECT * FROM photo ORDER BY datetime(date) ASC")
    fun listOrderedByDate(): LiveData<List<Photo>>

    @Query(value = "SELECT * FROM photo ORDER BY title ASC")
    fun listOrderedByName(): LiveData<List<Photo>>

    @Query(value = "SELECT * FROM photo ORDER BY datetime(date) ASC LIMIT :limit ")
    fun listOf(limit: Long): LiveData<List<Photo>>

    @Query("SELECT * from photo WHERE id = :id")
    fun get(id: Long): Photo?

    @Query("DELETE FROM photo")
    fun clear()

    @Delete
    suspend fun delete(p: Photo)

}