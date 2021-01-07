package de.maggiwuerze.nextcloudgallery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import de.maggiwuerze.nextcloudgallery.ViewItem
import de.maggiwuerze.nextcloudgallery.persistence.model.album.Album
import de.maggiwuerze.nextcloudgallery.model.album.AlbumRepository
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import de.maggiwuerze.nextcloudgallery.persistence.model.DatabaseManager
import kotlinx.coroutines.launch
import org.threeten.bp.YearMonth

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    var currentPosition: Int = 0
    var navVisible: Boolean = true
    internal val gallery = MediatorLiveData<ArrayList<ViewItem>>()
    private val photoRepository =
        DatabaseManager.getInstance().photoRepository
    private var albumRepository: AlbumRepository =
        DatabaseManager.getInstance().albumRepository
    private var albums: LiveData<List<Album>>

    init {

        var sort = PreferenceManager.getDefaultSharedPreferences(application)
            .getString("gallery_sort", "Date")

        val source = when (sort) {
            "Date" -> photoRepository.allPhotosSortedByDate()
            "Name" -> photoRepository.allPhotosSortedByName()
            "Filesize" -> photoRepository.allPhotosSortedByDate()
            "Resolution" -> photoRepository.allPhotosSortedByDate()
            else -> photoRepository.allPhotosSortedByDate()
        }

        gallery.addSource(source) { result: List<Photo>? ->
            result?.let {
                gallery.value = buildGalleryListFromImages(it, sort)
            }
        }


        albums = albumRepository.allAlbums
    }

    private fun buildGalleryListFromImages(
        allPhotos: List<Photo>,
        sort: String?
    ): ArrayList<ViewItem> {

        return when (sort) {
            "Name" -> getGalleryListByName(allPhotos)
            else -> getGalleryListByDate(allPhotos)
        }

    }

    fun insert(photo: Photo) = viewModelScope.launch {
        photoRepository.insert(photo)
    }

    fun insert(album: Album) = viewModelScope.launch {
        albumRepository.insert(album)
    }

    private fun getGalleryListByDate(allPhotos: List<Photo>): ArrayList<ViewItem> {

        val galleryList = ArrayList<ViewItem>()
        var previousYearMonth: YearMonth? = null
        allPhotos.forEach {

            var currentYearMonth = YearMonth.from(it.date)
            when {
                previousYearMonth == null -> {
                    galleryList.add(ViewItem.DateItem(it))
                    galleryList.add(ViewItem.ImageItem(it))
                }
                previousYearMonth!! == currentYearMonth -> {
                    galleryList.add(ViewItem.ImageItem(it))
                }
                previousYearMonth!!.isBefore(currentYearMonth) -> {
                    galleryList.add(ViewItem.DateItem(it))
                    galleryList.add(ViewItem.ImageItem(it))
                }
            }
            previousYearMonth = currentYearMonth
        }

        return galleryList
    }

    private fun getGalleryListByName(allPhotos: List<Photo>): ArrayList<ViewItem> {

        val galleryList = ArrayList<ViewItem>()
        var previousLetter = ""
        allPhotos.forEach {
            var currentLetter: String = it.title[0].toString()
            when (previousLetter) {
                "" -> {
                    galleryList.add(ViewItem.NameItem(it))
                    galleryList.add(ViewItem.ImageItem(it))
                }
                currentLetter -> {
                    galleryList.add(ViewItem.ImageItem(it))
                }
                else -> {
                    galleryList.add(ViewItem.NameItem(it))
                    galleryList.add(ViewItem.ImageItem(it))
                }
            }
            previousLetter = currentLetter
        }

        return galleryList
    }

}