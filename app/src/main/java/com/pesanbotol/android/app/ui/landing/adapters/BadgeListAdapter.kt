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
import com.pesanbotol.android.app.databinding.BadgeItemBinding
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.search.interfaces.BottleItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class BadgeListAdapter(
    private val badges: ArrayList<String?>,
) :
    RecyclerView.Adapter<BadgeListAdapter.BadgeListAdapterViewHolder>() {
    class BadgeListAdapterViewHolder(
        private val view: BadgeItemBinding
    ) :
        RecyclerView.ViewHolder(view.root) {
        fun binding(
            badge: String?,
        ) {
            view.rewardName.text = badge

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BadgeListAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingv2 = BadgeItemBinding.inflate(inflater, parent, false)
        return BadgeListAdapterViewHolder(bindingv2)

    }

    override fun onBindViewHolder(holder: BadgeListAdapterViewHolder, position: Int) {
        holder.binding(
            badges[position],
        )
    }

    override fun getItemCount(): Int = badges.size
}