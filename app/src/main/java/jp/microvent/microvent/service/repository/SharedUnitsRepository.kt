package jp.microvent.microvent.service.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.Units

class SharedUnitsRepository(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.unit_pref),
            Context.MODE_PRIVATE
        )
    }


    /**
     * 与えられた測定項目文字列の単位を取得
     * strings.xmlで測定項目文字列定義
     */
    fun unitOf(label: String): String? = sharedPreferences.run {
        getString(label, null)
    }

    /**
     * 単位をsharedPrefに格納
     */
    fun putUnits(units: Units) {
        with(sharedPreferences.edit()) {
            putString("height", units.height)
            putString("weight", units.weight)
            putString("air_flow", units.airFlow)
            putString("o2_flow", units.o2Flow)
            putString("total_flow", units.totalFlow)
            putString("estimated_mv", units.estimatedMv)
            putString("airway_pressure", units.airwayPressure)
            putString("estimated_peep", units.estimatedPeep)
            putString("vt_per_kg", units.vtPerKg)
            putString("predicted_vt", units.predictedVt)
            putString("estimated_vt", units.estimatedVt)
            putString("i", units.i)
            putString("e", units.e)
            putString("i_avg", units.iAvg)
            putString("e_avg", units.eAvg)
            putString("rr", units.rr)
            putString("fio2", units.fio2)
            putString("spo2", units.spo2)
            putString("etco2", units.etco2)
            putString("pao2", units.pao2)
            putString("paco2", units.paco2)
            commit()
        }
    }

    /**
     * 実際に表示する文字列に単位をsharedPrefから適用する\
     * layoutLiveData:layout.xmlとリンクしているlivedata->実際に表示される文字列,%1$sが代替文字列であるが、これが含まれない場合、末尾に追記する
     * stringValue:string.xmlとリンクしている文字列。これを加工して実際に表示される文字列が決まる
     * prefKey:sharedPreferencesで定義されている測定値のキー
     */
    fun setUnit(layoutLiveData: MutableLiveData<String>, stringValue: String, prefKey: String) {
        val regex = Regex("%1")
        val str: String = if (regex.containsMatchIn(stringValue)) {
            stringValue
        } else {
            stringValue + " %s"
        }
        val unit = sharedPreferences.getString(prefKey, null)
        val netString = String.format(str, unit)
        layoutLiveData.postValue(netString)
    }

    fun resetAll(){
        sharedPreferences.edit().run{
            clear()
            commit()
        }
    }
}