package de.maggiwuerze.nextcloudgallery.model.album

import androidx.lifecycle.LiveData
import de.maggiwuerze.nextcloudgallery.model.account.Account

class AccountRepository(private val accountDao: AccountDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allAccounts: LiveData<List<Account>> = accountDao.getAllAccounts()

    fun insert(word: Account) {
        accountDao.insert(word)
    }

    fun accountAlreadyExists(username: String, url: String): Boolean {

        val accountByUsernameAndPassword = accountDao.accountByUsernameAndPassword(username, url)
        return accountByUsernameAndPassword.isNotEmpty()

    }

    fun activeAccount(): Account {

        return accountDao.activeAccount()

    }

}