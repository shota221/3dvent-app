package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class UsedPlace {
    UNSELECTED{
        override fun getString(context: Context): String = context.getString(R.string.unselected)
    },
    AMBULANCE{
        override fun getString(context: Context): String = context.getString(R.string.ambulance)
    },
    EMERGENCY_ROOM{
        override fun getString(context: Context): String = context.getString(R.string.emergency_room)
    },
    ICU{
        override fun getString(context: Context): String = context.getString(R.string.icu)
    },
    OPERATING_ROOM{
        override fun getString(context: Context): String = context.getString(R.string.operating_room)
    },
    MRI{
        override fun getString(context: Context): String = context.getString(R.string.mri)
    },
    OTHER_LABORATORIES{
        override fun getString(context: Context): String = context.getString(R.string.other_laboratories)
    },
    NON_ICU{
        override fun getString(context: Context): String = context.getString(R.string.non_icu)
    },
    OTHER_PLACES{
        override fun getString(context: Context): String = context.getString(R.string.other_places)
    };

    abstract fun getString(context: Context):String


    companion object{
        /**
         * 逆引き用
         */
        fun buildUsedPlace(i :Int?):UsedPlace?{
            UsedPlace.values().forEach{
                if(it.ordinal == i) return it
            }
            return null
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return UsedPlace.values().map {
                it.getString(context)
            }
        }
    }
}