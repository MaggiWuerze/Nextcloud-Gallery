package de.maggiwuerze.nextcloudgallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.maggiwuerze.nextcloudgallery.persistence.model.remotefolder.RemoteFolder
import de.maggiwuerze.nextcloudgallery.util.TreeFolder

class OnboardingViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    var remoteRootFolder: TreeFolder? = null
    var folderList = mutableListOf<TreeFolder>()
    var user = ""
    var password = ""
    var url = ""
    val text: LiveData<String> = Transformations.map(_index) {
        "Coming Soon: A Folder Tree with : $it Folders"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

}