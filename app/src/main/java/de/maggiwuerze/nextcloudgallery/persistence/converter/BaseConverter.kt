package de.maggiwuerze.nextcloudgallery.persistence.converter

import androidx.room.TypeConverter

/**
 * Base converter to convert an object to a String and vice-versa for Room
 * @author Julien Guerinet
 * @since 4.2.0
 */
abstract class BaseConverter<T> {

    /**
     * Converts the [value] to a String. The default implementation is to call [toString].
     *  This can be overridden.
     */
    @TypeConverter
    open fun toString(value: T?): String? = value?.toString()

    /**
     * Converts the [value] to a [T]. If the [value] is null or empty, the returned [T] is null.
     *  If not, this called [objectFromString]. This can be overridden.
     */
    @TypeConverter
    open fun fromString(value: String?): T? =
        if (value.isNullOrEmpty()) null else objectFromString(value)

    /**
     * Converts the[value] to a [T]
     */
    abstract fun objectFromString(value: String): T?

    /**
     * Converts the [value] to a List of [T]
     */
    abstract fun objectListFromString(value: String): List<T>?

    /**
     * Converts the List [value] to a String [T]
     */
    abstract fun stringFromObjectList(value: List<T>?): String

    @TypeConverter
    open fun stringToSomeObjectList(value: String?): List<T>? =
        if (value.isNullOrEmpty()) null else objectListFromString(value)

    @TypeConverter
    open fun objectListToString(value: List<T>?): String =
        if (value.isNullOrEmpty()) "" else stringFromObjectList(value)

}