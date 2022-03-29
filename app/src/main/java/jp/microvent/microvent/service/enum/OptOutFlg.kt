package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class OptOutFlg {
    OFF{
        override fun getString(context: Context): String = context.getString(R.string.off)
    },
    ON{
        override fun getString(context: Context): String = context.getString(R.string.on)
    };

    abstract fun getString(context: Context):String

    companion object{
        /**
         * 逆引き用
         */
        fun buildOptOutFlg(i: Int?): OptOutFlg? {
            return OptOutFlg.values().firstOrNull {
                it.ordinal == i
            }
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return OptOutFlg.values().map {
                it.getString(context)
            }
        }
    }

}