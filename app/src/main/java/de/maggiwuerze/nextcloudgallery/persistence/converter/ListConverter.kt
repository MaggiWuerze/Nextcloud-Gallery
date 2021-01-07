package de.maggiwuerze.nextcloudgallery.persistence.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder
import java.lang.reflect.Type
import java.util.*

class ListConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<RemoteFolder> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<RemoteFolder?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<RemoteFolder?>?): String {
        return Gson().toJson(someObjects)
    }
}