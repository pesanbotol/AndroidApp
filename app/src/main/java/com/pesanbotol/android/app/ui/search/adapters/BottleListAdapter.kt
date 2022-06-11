package com.pesanbotol.android.app.ui.search.adapters

import android.location.Address
import android.location.Geocoder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.search.model.BottleItems
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.search.interfaces.BottleItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class BottleListAdapter(
    private val bottles: ArrayList<BottleItems?>,
    private val clickListener: BottleItemClickListener
) :
    RecyclerView.Adapter<BottleListAdapter.MissionListAdapterViewHolder>() {
    class MissionListAdapterViewHolder(
        private val v2: ItemSamePlaceBinding
    ) :
        RecyclerView.ViewHolder(v2.root) {
        fun binding(
            bottle: BottleItems?,
            clickListener: BottleItemClickListener
        ) {
            Glide.with(v2.root).load(bottle?.document?.contentImageMediaThumbnailUrl)
                .apply(
                    RequestOptions().error(
                        ContextCompat.getDrawable(
                            v2.root.context,
                            R.drawable.empty_profile,
                        ),
                    )
                )
                .into(v2.imageDialogProfile)
            v2.titleUsername.text = bottle?.document?.contentText ?: "-"
            val time = (bottle?.document?.createdAt ?: System.currentTimeMillis()).toLong() * 1000
            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            bottle?.document?.geo.let {
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
            v2.tvTimeUpload.text = niceDateStr
            v2.root.setOnClickListener {
                bottle?.apply {
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
            bottles[position],
            clickListener
        )
    }

    override fun getItemCount(): Int = bottles.size
}