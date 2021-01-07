package de.maggiwuerze.nextcloudgallery.persistence.model.photo

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import de.maggiwuerze.nextcloudgallery.util.KParcelable
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Entity(tableName = "photo", indices = [Index(value = ["url"], unique = true)])
data class Photo(
    var title: String,
    var description: String?,
    var url: String,
    var date: LocalDateTime
) : KParcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        description = parcel.readString(),
        url = parcel.readString() ?: "",
        date = LocalDateTime.parse(parcel.readString(), DateTimeFormatter.ISO_DATE_TIME)
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(date.format(DateTimeFormatter.ISO_DATE_TIME))
        parcel.writeInt(id)
    }

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(parcel: Parcel): Photo {
                return Photo(parcel)
            }

            override fun newArray(size: Int): Array<Photo?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Photo
        if (url != other.url) return false
        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

    fun previewUrl(baseUri: String): String {

        var relativePath = url.removePrefix("/remote.php/webdav")
        return "$baseUri/core/preview.png?file=$relativePath&x=300&y=300&a=true"

    }

}