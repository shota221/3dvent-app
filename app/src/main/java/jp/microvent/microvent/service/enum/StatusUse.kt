package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class StatusUse {
    UNSELECTED{
        override fun getString(context: Context): String = context.getString(R.string.unselected)
              },
    RESPIRATORY_FAILURE{
        override fun getString(context: Context): String = context.getString(R.string.respiratory_failure)
        },
    SURGERY{
        override fun getString(context: Context): String = context.getString(R.string.surgery)
    },
    INSPECTION_PROCEDURE{
        override fun getString(context: Context): String = context.getString(R.string.inspection_procedure)
    },
    OTHER{
        override fun getString(context: Context): String = context.getString(R.string.other)
    };

    abstract fun getString(context: Context):String


    companion object{
        /**
         * 逆引き用
         */
        fun buildStatusUse(i: Int?): StatusUse? {
            return StatusUse.values().firstOrNull {
                it.ordinal == i
            }
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return StatusUse.values().map {
                it.getString(context)
            }
        }
    }

}