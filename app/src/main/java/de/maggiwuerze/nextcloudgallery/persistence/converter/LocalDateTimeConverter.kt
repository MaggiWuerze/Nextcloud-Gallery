package de.maggiwuerze.nextcloudgallery.persistence.converter

import org.threeten.bp.LocalDateTime

class LocalDateTimeConverter : BaseConverter<LocalDateTime>() {
    override fun objectFromString(value: String): LocalDateTime? = LocalDateTime.parse(value)

    override fun objectListFromString(value: String): List<LocalDateTime>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stringFromObjectList(value: List<LocalDateTime>?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}