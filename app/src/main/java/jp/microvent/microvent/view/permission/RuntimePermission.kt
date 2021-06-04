package jp.microvent.microvent.view.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build


object RuntimePermissionUtils {
    fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            if (context.checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun checkGrantResults(vararg grantResults: Int): Boolean {
        require(grantResults.size != 0) { "grantResults is empty" }
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}