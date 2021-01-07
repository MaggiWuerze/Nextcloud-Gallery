package de.maggiwuerze.nextcloudgallery.persistence.model.album

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    var title: String,
    var description: String,
    var thumbnailPath: String?
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}