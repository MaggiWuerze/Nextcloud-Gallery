package de.maggiwuerze.nextcloudgallery

import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo

sealed class ViewItem(val resource: Int) {

    class DateItem(val photo: Photo) : ViewItem(R.layout.date_seperator)
    class NameItem(val photo: Photo) : ViewItem(R.layout.date_seperator)
    class ImageItem(val photo: Photo) : ViewItem(R.layout.photo_card)


}