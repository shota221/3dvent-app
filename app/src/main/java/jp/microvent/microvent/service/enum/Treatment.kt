package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class Treatment {
    UNSELECTED{
        override fun getString(context: Context): String = context.getString(R.string.unselected)
              },
    NONE{
        override fun getString(context: Context): String = context.getString(R.string.none)
        },
    OXYGEN_ONLY{
        override fun getString(context: Context): String = context.getString(R.string.oxygen_only)
    },
    NPPV{
        override fun getString(context: Context): String = context.getString(R.string.nppv)
    },
    OTHER_VENT{
        override fun getString(context: Context): String = context.getString(R.string.other_vent)
    },
    ECMO{
        override fun getString(context: Context): String = context.getString(R.string.ecmo)
    },
    OTHER{
        override fun getString(context: Context): String = context.getString(R.string.other)
    };

    abstract fun getString(context: Context):String

    companion object{
        /**
         * 逆引き用
         */
        fun buildTreatment(i: Int?): Treatment? {
            return Treatment.values().firstOrNull {
                it.ordinal == i
            }
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return Treatment.values().map {
                it.getString(context)
            }
        }
    }

}