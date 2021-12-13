package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class Outcome {
    UNSELECTED{
        override fun getString(context: Context): String = context.getString(R.string.unselected)
    },
    IMPROVEMENT{
        override fun getString(context: Context): String = context.getString(R.string.improvement)
    },
    IMMUTABLE{
        override fun getString(context: Context): String = context.getString(R.string.immutable)
    },
    DETERIORATION{
        override fun getString(context: Context): String = context.getString(R.string.deterioration)
    },
    DEATH{
        override fun getString(context: Context): String = context.getString(R.string.death)
    };

    abstract fun getString(context: Context):String


    companion object{
        /**
         * 逆引き用
         */
        fun buildOutcome(i: Int?): Outcome? {
            return Outcome.values().firstOrNull {
                it.ordinal == i
            }
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return Outcome.values().map {
                it.getString(context)
            }
        }
    }
}