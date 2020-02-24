package br.com.fernandosousa.lmsapp

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    // solicitar permissão
    fun validate(activity: Activity, requestCode: Int,
                 vararg permissions: String): Boolean {
        val list = ArrayList<String>()
        // validar quais permissões já existem
        for (permission in permissions) {
            val ok = ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED
            if (!ok) {
                list.add(permission)
            }
        }
        // se todas já foram permitidas, retorna true
        if (list.isEmpty()) return true

        // caso exista alguma pendência, solicita a permissão
        val newP = arrayOfNulls<String>(list.size)
        list.toArray(newP)
        ActivityCompat.requestPermissions(activity, newP, requestCode)
        return false
    }
}