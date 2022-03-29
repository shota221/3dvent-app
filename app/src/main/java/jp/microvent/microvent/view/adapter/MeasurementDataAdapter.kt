package jp.microvent.microvent.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.MeasurementDataListItemBinding
import jp.microvent.microvent.service.model.VentilatorValueListElm
import jp.microvent.microvent.viewModel.MeasurementDataListViewModel

class MeasurementDataAdapter(private val viewModel: MeasurementDataListViewModel) :
    RecyclerView.Adapter<MeasurementDataAdapter.MeasurementDataViewHolder>() {

    private var measurementDataList: List<VentilatorValueListElm>? = null

    fun setMeasurementDataList(measurementDataList: List<VentilatorValueListElm>) {

        if (this.measurementDataList == null) {
            this.measurementDataList = measurementDataList
            notifyItemRangeInserted(0, measurementDataList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@MeasurementDataAdapter.measurementDataList).size
                }

                override fun getNewListSize(): Int {
                    return measurementDataList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldList = this@MeasurementDataAdapter.measurementDataList
                    return oldList?.get(oldItemPosition)?.id == measurementDataList[newItemPosition].id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val project = measurementDataList[newItemPosition]
                    val old = measurementDataList[oldItemPosition]
                    return project.id == old.id
                }
            })
            this.measurementDataList = measurementDataList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): MeasurementDataViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.measurement_data_list_item, parent,
            false
        ) as MeasurementDataListItemBinding
        binding.measurementDataListViewModel = viewModel
        return MeasurementDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeasurementDataViewHolder, position: Int) {
        holder.binding.ventilatorValue = measurementDataList?.get(position)
        holder.binding.executePendingBindings()
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = measurementDataList?.size ?: 0

    open class MeasurementDataViewHolder(val binding: MeasurementDataListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

