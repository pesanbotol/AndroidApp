package com.pesanbotol.android.app.ui.landing.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.bottle.model.MissionsItem
import com.pesanbotol.android.app.databinding.ItemMissionBinding
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.landing.`interface`.MissionItemClickListener
import com.pesanbotol.android.app.ui.landing.`interface`.SamePlaceItemClickListener

class MissionListAdapter(
    private val mission: ArrayList<MissionsItem>,
    private val clickListener: MissionItemClickListener,
) :
    RecyclerView.Adapter<MissionListAdapter.MissionListAdapterViewHolder>() {
    class MissionListAdapterViewHolder(private val v: ItemMissionBinding) :
        RecyclerView.ViewHolder(v.root) {
        fun binding(item: MissionsItem, clickListener: MissionItemClickListener) {
            v.titleUsername.text = if (item.description != null) "${item.description}" else ""
            v.snippetDesc.text =
                    if (item.reward != null) "Tugas : Kunjungi tempat ini, untuk mendapatkan badge ${item.reward}" else ""
            v.markAsDone.setOnClickListener {
                clickListener.onClick(item)
            }
            val time = (item.createdAt ?: System.currentTimeMillis()).toLong() * 1000
            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            v.tvTimeUpload.text = niceDateStr
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionListAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMissionBinding.inflate(inflater, parent, false)
        return MissionListAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MissionListAdapterViewHolder, position: Int) {
        holder.binding(mission[position], clickListener)
    }

    override fun getItemCount(): Int = mission.size
}