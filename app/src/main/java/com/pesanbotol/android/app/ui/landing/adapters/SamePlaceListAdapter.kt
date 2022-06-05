package com.pesanbotol.android.app.ui.landing.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.landing.`interface`.SamePlaceItemClickListener

class SamePlaceListAdapter(
    private val bottles: ArrayList<BottleItem>,
    private val clickListener: SamePlaceItemClickListener,
) :
    RecyclerView.Adapter<SamePlaceListAdapter.SamePlaceViewHolder>() {
    class SamePlaceViewHolder(private val v: ItemSamePlaceBinding) :
        RecyclerView.ViewHolder(v.root) {
        fun binding(item: BottleItem) {
            Glide.with(v.root).load(item.contentImage?.mediaThumbnailUrl).into(v.imageDialogProfile)
            v.titleUsername.text = item.user?.username ?: "-"
            v.snippetDesc.text = item.contentText
            val time = (item.createdAt ?: System.currentTimeMillis()).toLong() * 1000
            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            v.tvTimeUpload.text = niceDateStr
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamePlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSamePlaceBinding.inflate(inflater, parent, false)
        return SamePlaceViewHolder(binding)

    }

    override fun onBindViewHolder(holder: SamePlaceViewHolder, position: Int) {
        holder.binding(bottles[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(bottles[position])
        }
    }

    override fun getItemCount(): Int = bottles.size
}