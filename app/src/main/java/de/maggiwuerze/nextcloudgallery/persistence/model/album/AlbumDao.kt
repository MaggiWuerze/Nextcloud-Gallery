package de.maggiwuerze.nextcloudgallery.model.album

import androidx.lifecycle.LiveData
import androidx.room.*
import de.maggiwuerze.nextcloudgallery.persistence.model.album.Album


@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Album): Long

    @Update
    fun update(note: Album)

    @Transaction
    fun upsert(obj: Album) {
        val id = insert(obj)
        if (id == -1L) {
            update(obj)
        }
    }

    @Query("DELETE FROM album")
    fun deleteAllAlbums()

    @Query("SELECT * FROM album ")
    fun getAllAlbums(): LiveData<List<Album>>

}