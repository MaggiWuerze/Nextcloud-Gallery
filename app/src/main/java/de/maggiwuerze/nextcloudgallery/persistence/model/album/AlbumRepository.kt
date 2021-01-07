package de.maggiwuerze.nextcloudgallery.model.album

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import de.maggiwuerze.nextcloudgallery.persistence.model.album.Album

class AlbumRepository(private val albumDao: AlbumDao) {

    val allAlbums: LiveData<List<Album>> = albumDao.getAllAlbums()


    fun insert(album: Album) {
        InsertAlbumsAsyncTask(albumDao).execute(album)
    }

    fun deleteAllAlbums() {
        DeleteAllAlbumsAsyncTask(albumDao).execute()
    }

    private class InsertAlbumsAsyncTask(albumDao: AlbumDao) : AsyncTask<Album, Unit, Unit>() {
        val albumDao = albumDao

        override fun doInBackground(vararg arrayOfAlbums: Album?) {
            albumDao.insert(arrayOfAlbums[0]!!)
        }
    }


    private class DeleteAllAlbumsAsyncTask(val albumDao: AlbumDao) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            albumDao.deleteAllAlbums()
        }
    }

}