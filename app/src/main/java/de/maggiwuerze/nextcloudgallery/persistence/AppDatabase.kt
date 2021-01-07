package de.maggiwuerze.nextcloudgallery.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.maggiwuerze.nextcloudgallery.model.account.Account
import de.maggiwuerze.nextcloudgallery.model.album.AccountDao
import de.maggiwuerze.nextcloudgallery.persistence.model.album.Album
import de.maggiwuerze.nextcloudgallery.model.album.AlbumDao
import de.maggiwuerze.nextcloudgallery.persistence.converter.ListConverter
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.PhotoDao
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolderDao
import de.maggiwuerze.nextcloudgallery.persistence.converter.LocalDateTimeConverter


@Database(entities = [Album::class, Photo::class, Account::class, RemoteFolder::class], version = 1)
@TypeConverters(value = [LocalDateTimeConverter::class, ListConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun accountDao(): AccountDao
    abstract fun photoDao(): PhotoDao
    abstract fun remoteFolderDao(): RemoteFolderDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gallery_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}