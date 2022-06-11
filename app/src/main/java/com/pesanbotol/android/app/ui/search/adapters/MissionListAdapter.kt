package com.pesanbotol.android.app.ui.search.adapters

import android.location.Address
import android.location.Geocoder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.search.model.MissionItems
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.search.interfaces.MissionItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class MissionListAdapter(
    private val missions: ArrayList<MissionItems?>,
    private val clickListener: MissionItemClickListener
) :
    RecyclerView.Adapter<MissionListAdapter.MissionListAdapterViewHolder>() {
    class MissionListAdapterViewHolder(
        private val v2: ItemSamePlaceBinding
    ) :
        RecyclerView.ViewHolder(v2.root) {
        fun binding(
            mission: MissionItems?,
            clickListener: MissionItemClickListener
        ) {
            v2.imageDialogProfile.visibility = View.GONE
            v2.titleUsername.text = mission?.document?.description ?: "-"
//            val time = (mission?.document?.createdAt ?: System.currentTimeMillis()).toLong() * 1000
//            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
//                time,
//                System.currentTimeMillis(),
//                DateUtils.MINUTE_IN_MILLIS
//            ) as String
            mission?.document?.center.let {
                val geocoder = Geocoder(v2.root.context, Locale.getDefault())
                val addresses: List<Address> =
                        geocoder.getFromLocation(it!![0]!!, it[1]!!, 1)
                if (addresses.isNotEmpty()) {
                    val address: String = addresses[0].getAddressLine(0) ?: ""
                    val city: String = addresses[0].locality ?: ""
                    val state: String = addresses[0].adminArea ?: ""
//                    val zip: String = addresses[0].postalCode
                    val country: String = addresses[0].countryName ?: ""
                    val location = "$address, $city, $state"
                    v2.snippetDesc.text = location
                }
            }
            v2.tvTimeUpload.text = ""
            v2.root.setOnClickListener {
                mission?.apply {
                    clickListener.onClick(this)
                }

            }

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionListAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingv2 = ItemSamePlaceBinding.inflate(inflater, parent, false)
        return MissionListAdapterViewHolder(bindingv2)

    }

    override fun onBindViewHolder(holder: MissionListAdapterViewHolder, position: Int) {
        holder.binding(
            missions[position],
            clickListener
        )
    }

    override fun getItemCount(): Int = missions.size
}