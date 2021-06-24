package jp.microvent.microvent.service.enum

import android.content.Context
import jp.microvent.microvent.R

/**
 * 多言語化用Enum
 */
enum class AdverseEventFlg {
    NONE {
        override fun getString(context: Context): String = context.getString(R.string.none)
    },
    EXISTS {
        override fun getString(context: Context): String = context.getString(R.string.exists)
    };

    abstract fun getString(context: Context):String


    companion object{
        /**
         * 逆引き用
         */
        fun buildAdverseEventFlg(i :Int?):AdverseEventFlg?{
            AdverseEventFlg.values().forEach{
                if(it.ordinal == i) return it
            }
            return null
        }

        /**
         * スピナーセット用
         */
        fun getStringList(context: Context):List<String>{
            return AdverseEventFlg.values().map {
                it.getString(context)
            }
        }
    }
}