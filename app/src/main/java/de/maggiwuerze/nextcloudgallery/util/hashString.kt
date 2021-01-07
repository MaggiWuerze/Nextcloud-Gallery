package de.maggiwuerze.nextcloudgallery.util

import java.math.BigInteger
import java.security.MessageDigest

fun String.hashString(): String =
    this.hashString("MD5")

fun String.sha512(): String =
    this.hashString("SHA-512")

fun String.sha256(): String =
    this.hashString("SHA-256")

fun String.sha1(): String =
    this.hashString("SHA-1")


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(16, '0')
}

fun String.hashString(type: String): String {
    return MessageDigest
        .getInstance(type)
        .digest(toByteArray())
        .joinToString(separator = "", limit = 16) {
            Integer.toHexString(
                it.toInt()
            )
        }
}