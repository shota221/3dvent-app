package jp.microvent.microvent.service.repository

import jp.microvent.microvent.service.model.User
import retrofit2.Response

/**
 * ViewModelに対するデータプロバイダ
 */
class UserRepository {

    companion object Factory {
        val instance: UserRepository
        @Synchronized get() {
            return UserRepository()
        }
    }
}