package com.terabyte.mediastorage.util

import com.terabyte.mediastorage.json.UserJson

object UserManager {
    var user: UserJson? = null
        private set

    fun cache(user: UserJson) {
        this.user = user
    }


}