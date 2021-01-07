package de.maggiwuerze.nextcloudgallery.util

import com.thegrizzlylabs.sardineandroid.DavResource

class TreeFolder(
    var davResource: DavResource
) {
    var children: MutableList<TreeFolder> = mutableListOf()

    override fun toString(): String {
        return davResource.name
    }

}