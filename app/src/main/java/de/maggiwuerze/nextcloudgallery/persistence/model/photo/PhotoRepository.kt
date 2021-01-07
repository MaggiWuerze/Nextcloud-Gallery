package de.maggiwuerze.nextcloudgallery.persistence.model.photo

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.get
import de.maggiwuerze.nextcloudgallery.util.md5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PhotoRepository(private val photoDao: PhotoDao, val context: Context) {

    //    val allPhotos: LiveData<List<Photo>> = photoDao.listOf(100)
    fun allPhotosSortedByName(): LiveData<List<Photo>> = photoDao.listOrderedByName()

    fun allPhotosSortedByDate(): LiveData<List<Photo>> = photoDao.listOrderedByDate()

    fun insert(photo: Photo) = GlobalScope.async {
        var id = photoDao.insert(photo)
        if (id != -1L) {
//            var hash = photo.url.hashString()
            var hash = photo.url.md5()
            saveThumbnailToDisk(hash, photo.previewUrl("https://cloud.maggiwuerze.de"))
        }
    }


    fun delete(photo: Photo) = GlobalScope.async {

        photoDao.delete(photo)
    }

    private suspend fun saveThumbnailToDisk(hash: String, url: String) =
        withContext(Dispatchers.IO) {

            var directory =
                ContextWrapper(context)
                    .getDir("thumbs", Context.MODE_PRIVATE)
            var file = File(directory, "${hash}")

            if (file.exists()) {
                throw RuntimeException()
            }

            try {

                var drawable = Coil.get(url)

                FileOutputStream(file).use { out ->
                    drawable.toBitmap().compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        out
                    )
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
}


