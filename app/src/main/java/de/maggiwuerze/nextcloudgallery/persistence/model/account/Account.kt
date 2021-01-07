package de.maggiwuerze.nextcloudgallery.model.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    var username: String,
    var password: String,
    var active: Boolean,
    var url: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}