package jp.microvent.microvent.service.model

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class Gender {
    UNSELECTED{
        override fun getString(context: Context): String = context.getString(R.string.unselected)
              },
    MALE{
        override fun getString(context: Context): String = context.getString(R.string.male)
        },
    FEMALE{
        override fun getString(context: Context): String = context.getString(R.string.female)
    };

    abstract fun getString(context: Context):String

    /**
     * 逆引き用
     */
    companion object{
        fun buildGender(i :Int?):Gender?{
            Gender.values().forEach{
                if(it.ordinal == i) return it
            }
            return null
        }
    }

}