package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum:Todo共通化
 */
enum class AdverseEventFlg {
    NONE {
        override fun getString(context: Context): String = context.getString(R.string.none)
    },
    EXISTS {
        override fun getString(context: Context): String = context.getString(R.string.exists)
    };

    abstract fun getString(context: Context): String


    companion object {
        /**
         * 逆引き用
         */
        fun buildAdverseEventFlg(i: Int?): AdverseEventFlg? {
            return AdverseEventFlg.values().firstOrNull {
                it.ordinal == i
            }
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context): List<String> {
            return AdverseEventFlg.values().map {
                it.getString(context)
            }
        }
    }
}