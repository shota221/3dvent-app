package jp.microvent.microvent.service.model

import android.content.Context
import jp.microvent.microvent.R

enum class RecordStatus{
    WAITING {
        override fun getStatusString(context: Context) = context.getString(R.string.waiting)
        override fun getButtonString(context: Context) = context.getString(R.string.rec)
        override fun next() = RECORDING
    },
    RECORDING {
        override fun getStatusString(context: Context) = context.getString(R.string.recording)
        override fun getButtonString(context: Context) = context.getString(R.string.stop)
        override fun next() = WAITING
    };

    abstract fun getStatusString(context: Context):String
    abstract fun getButtonString(context: Context):String
    abstract fun next():RecordStatus
}