package de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder

import de.maggiwuerze.nextcloudgallery.util.TreeFolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RemoteFolderRepository(private val remoteFolderDao: RemoteFolderDao) {

    fun list(): List<RemoteFolder> = remoteFolderDao.list()

    fun insert(remoteFolder: RemoteFolder) = GlobalScope.async {
        remoteFolderDao.insert(remoteFolder)
    }

    fun insertAll(remoteFolders: MutableList<TreeFolder>) = GlobalScope.async {
        remoteFolders.forEach { remoteFolderDao.insert(RemoteFolder(it.davResource)) }
    }

    fun delete(photo: RemoteFolder) = GlobalScope.async {

        remoteFolderDao.delete(photo)
    }
}


