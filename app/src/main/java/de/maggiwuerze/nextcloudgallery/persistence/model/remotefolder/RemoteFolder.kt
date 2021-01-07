package de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.thegrizzlylabs.sardineandroid.DavResource
import de.maggiwuerze.nextcloudgallery.persistence.converter.ListConverter

@Entity(tableName = "remotefolder")
data class RemoteFolder(
    @Ignore
    val davResource: DavResource?
) {
    constructor(
        id: Long,
        name: String,
        path: String
    ) : this(null) {
        this.id = id
        this.name = name
        this.path = path
    }

    var name: String = davResource!!.name
    var path: String = davResource!!.path


    @PrimaryKey(autoGenerate = true)
    var id: Long = 0


    override fun toString(): String {
        return davResource!!.name
    }

}