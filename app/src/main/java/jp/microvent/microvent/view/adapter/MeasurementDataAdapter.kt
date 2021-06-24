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

//private object DiffCallback : DiffUtil.ItemCallback<VentilatorValueListElm>() {
//    override fun areItemsTheSame(oldItem: VentilatorValueListElm, newItem: VentilatorValueListElm): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: VentilatorValueListElm, newItem: VentilatorValueListElm): Boolean {
//        return oldItem == newItem
//    }
//}
//
//class VentilatorValueListAdapter (
//    private val viewLifecycleOwner: LifecycleOwner,
//    private val viewModel:MeasurementDataListViewModel
//        ) : ListAdapter<VentilatorValueListElm,VentilatorValueListAdapter.VentilatorValueViewHolder>(DiffCallback) {
//
//    class VentilatorValueViewHolder(private val binding: VentilatorValueViewBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: VentilatorValueListElm, viewLifecycleOwner: LifecycleOwner, viewModel: MeasurementDataListViewModel) {
//            binding.run {
//                lifecycleOwner = viewLifecycleOwner
//                ventilatorValue = item
//                this.measurementDataListViewModel= viewModel
//
////                executePendingBindings()
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentilatorValueViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return VentilatorValueViewHolder(VentilatorValueViewBinding.inflate(layoutInflater, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: VentilatorValueViewHolder, position: Int) {
//        holder.bind(getItem(position), viewLifecycleOwner, viewModel)
//    }
//}

//class MeasurementDataAdapter(private val dataSet: List<VentilatorValueListElm>) :
//    RecyclerView.Adapter<MeasurementDataAdapter.ViewHolder>() {
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val measurer: TextView
//        val registeredAt: TextView
//
//        init {
//            // Define click listener for the ViewHolder's View.
//            measurer = view.findViewById(R.id.tv_measurer_value)
//            registeredAt = view.findViewById(R.id.tv_registered_at_value)
//        }
//    }
//
//    // Create new views (invoked by the layout manager)
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        // Create a new view, which defines the UI of the list item
//        val view = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.measurement_data_list_item, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//        viewHolder.measurer.text = dataSet[position].registeredUserName
//        viewHolder.registeredAt.text = dataSet[position].registeredAt
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//}
//
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

