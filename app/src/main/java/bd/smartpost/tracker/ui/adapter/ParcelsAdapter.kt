package bd.smartpost.tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bd.smartpost.tracker.R
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.databinding.ParcelsItemviewBinding
import bd.smartpost.tracker.utils.ParcelState
import bd.smartpost.tracker.utils.ParcelTypes

class ParcelsAdapter(
    private val listener: ParcelListClickListener
):ListAdapter<Parcel, ParcelsAdapter.ParcelViewHolder>(ParcelDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelViewHolder =
        ParcelViewHolder(ParcelsItemviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ParcelViewHolder, position: Int) {
        holder.bind(getItem(position),holder.itemView)
    }

    inner class ParcelViewHolder(private val binding: ParcelsItemviewBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.apply {
                main.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        val curItem = getItem(adapterPosition)
                        val title = if (curItem.title.isNullOrBlank())  curItem.tracking_id else curItem.title
                        listener.onItemClick(title,getItem(adapterPosition))
                    }
                }
                parcelOptions.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        listener.onItemOptionClick(getItem(adapterPosition))
                    }
                }

            }

        }
        fun bind(parcel: Parcel, itemView: View){
            binding.apply {
                if (parcel.title?.isNotEmpty() == true){
                    headerFirst.text = parcel.title
                    headerSecond.text = parcel.tracking_id
                }else{
                    headerFirst.text = parcel.tracking_id
                    headerSecond.visibility = View.GONE
                }
                descriptionSecond.text = parcel.lastUpdateAt
                descriptionFirst.text = if (parcel.lastUpdate.isNullOrBlank())
                    itemView.context.getString(R.string.no_information_about_this_parcel) else parcel.lastUpdate

                when(parcel.type){
                    ParcelTypes.Domestic -> parcelImage.setImageResource(R.drawable.ic_circle_truck)
                    ParcelTypes.International -> parcelImage.setImageResource(R.drawable.ic_circle_plane)
                }

                when (parcel.state) {
                    ParcelState.Loading -> {
                        loader.visibility = View.VISIBLE
                        parcelImage.visibility = View.INVISIBLE
                        descriptionSection.visibility = View.GONE
                        trackingTextSection.visibility = View.VISIBLE

                    }
                    ParcelState.Updated -> {
                        loader.visibility = View.GONE
                        parcelImage.visibility = View.VISIBLE
                        descriptionSection.visibility = View.VISIBLE
                        trackingTextSection.visibility = View.GONE

                    }
                    ParcelState.Failed -> {
                        loader.visibility = View.GONE
                        parcelImage.visibility = View.VISIBLE
                        parcelImage.setImageResource(R.drawable.ic_circle_warning)
                        descriptionFirst.text =
                            itemView.context.getString(R.string.network_request_failed)
                        descriptionSection.visibility = View.VISIBLE
                        trackingTextSection.visibility = View.GONE
                    }
                }
            }
        }
    }

    interface ParcelListClickListener{
        fun onItemClick(title: String, parcel: Parcel)
        fun onItemOptionClick(parcel: Parcel)
    }

    class ParcelDiffUtils:DiffUtil.ItemCallback<Parcel>(){
        override fun areItemsTheSame(oldItem: Parcel, newItem: Parcel) = newItem.tracking_id == oldItem.tracking_id

        override fun areContentsTheSame(oldItem: Parcel, newItem: Parcel) = newItem.compare(oldItem)
    }
}
