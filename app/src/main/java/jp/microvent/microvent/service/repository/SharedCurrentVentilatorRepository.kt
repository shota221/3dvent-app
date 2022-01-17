package jp.microvent.microvent.service.repository

import android.content.Context
import android.content.SharedPreferences
import jp.microvent.microvent.R

class SharedCurrentVentilatorRepository(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.current_ventilator_pref),
            Context.MODE_PRIVATE
        )
    }

    /**
     * ventilatorId
     */
    var ventilatorId: Int?
        get() {
            val ventilatorId = sharedPreferences.getInt("id", 0)
            return if (ventilatorId == 0) {
                null
            } else {
                ventilatorId
            }
        }
        set(ventilatorId: Int?) {
            sharedPreferences.edit().run {
                ventilatorId?.let { putInt("id", it) }
                commit()
            }
        }

    /**
     * gs1Code
     */
    var gs1Code: String?
        get() {
            return sharedPreferences.getString("gs1Code", null)
        }
        set(gs1Code: String?) {
            sharedPreferences.edit().run {
                putString("gs1Code", gs1Code)
                commit()
            }
        }

    /**
     * patientId
     */
    var patientId: Int?
        get() {
            val patientId = sharedPreferences.getInt("patientId", 0)
            return if (patientId == 0) {
                null
            } else {
                patientId
            }
        }
        set(patientId: Int?) {
            sharedPreferences.edit().run {
                patientId?.let { putInt("patientId", it) }
                commit()
            }
        }

    /**
     * patientHeight
     */
    var patientHeight: String?
        get() {
            return sharedPreferences.getString("patientHeight", null)
        }
        set(patientHeight: String?) {
            sharedPreferences.edit().run {
                putString("patientHeight", patientHeight)
                commit()
            }
        }

    /**
     * patientGender
     */
    var patientGender: Int?
        get() {
            val patientGender = sharedPreferences.getInt("patientGender", 0)
            return if (patientGender == 0) {
                null
            } else {
                patientGender
            }
        }
        set(patientGender: Int?) {
            sharedPreferences.edit().run {
                patientGender?.let { putInt("patientGender", it) }
                commit()
            }
        }


    fun resetAll() {
        sharedPreferences.edit().run {
            clear()
            commit()
        }
    }
}