package de.maggiwuerze.nextcloudgallery.persistence.model

import android.content.Context
import de.maggiwuerze.nextcloudgallery.persistence.AppDatabase
import de.maggiwuerze.nextcloudgallery.model.album.AccountRepository
import de.maggiwuerze.nextcloudgallery.model.album.AlbumRepository
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.PhotoRepository
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolderRepository
import de.maggiwuerze.nextcloudgallery.util.SingletonHolder

class DatabaseManager private constructor() {

    private lateinit var appDatabase: AppDatabase
    lateinit var photoRepository: PhotoRepository
    lateinit var albumRepository: AlbumRepository
    lateinit var accountRepository: AccountRepository
    lateinit var remoteFolderRepository: RemoteFolderRepository

    fun initialize(context: Context) : DatabaseManager {
        this.appDatabase = AppDatabase.getDatabase(context)
        this.photoRepository = PhotoRepository(appDatabase.photoDao(), context)
        this.albumRepository = AlbumRepository(appDatabase.albumDao())
        this.accountRepository = AccountRepository(appDatabase.accountDao())
        this.remoteFolderRepository = RemoteFolderRepository(appDatabase.remoteFolderDao())
        return this
    }

    companion object : SingletonHolder<DatabaseManager, Context>(::DatabaseManager)
}