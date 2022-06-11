package com.pesanbotol.android.app.ui.landing.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.bottle.model.MissionsItem
import com.pesanbotol.android.app.data.core.model.BottleCustomMarker
import com.pesanbotol.android.app.databinding.ItemMissionBinding
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.landing.`interface`.MissionItemClickListener
import com.pesanbotol.android.app.ui.landing.`interface`.SamePlaceItemClickListener

class BottleCustomAdapter(
    private val mission: ArrayList<BottleCustomMarker>,
    private val missionClickListener: MissionItemClickListener,
    private val samePlaceClickListener: SamePlaceItemClickListener,
) :
    RecyclerView.Adapter<BottleCustomAdapter.MissionListAdapterViewHolder>() {
    class MissionListAdapterViewHolder(
        private val v: ItemMissionBinding?,
        private val v2: ItemSamePlaceBinding?
    ) :
        RecyclerView.ViewHolder(v?.root ?: v2!!.root) {
        fun binding(
            item: MissionsItem?,
            bottleItem: BottleItem?,
            missionClickListener: MissionItemClickListener,
            samePlaceClickListener: SamePlaceItemClickListener
        ) {
            if (item == null) {
                Glide.with(v2!!.root).load(bottleItem?.contentImage?.mediaThumbnailUrl)
                    .into(v2.imageDialogProfile)
                v2.titleUsername.text = bottleItem?.user?.username ?: "-"
                v2.snippetDesc.text = bottleItem?.contentText
                val time = (bottleItem?.createdAt ?: System.currentTimeMillis()).toLong() * 1000
                val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                    time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                ) as String
                v2.tvTimeUpload.text = niceDateStr
                v2.root.setOnClickListener {
                    bottleItem?.apply {
                        samePlaceClickListener.onClick(this)
                    }

                }
            } else {
                v?.titleUsername?.text = if (item.description != null) "Misi : ${item.description}" else ""
                v?.snippetDesc?.text =
                        if (item.reward != null) "Tugas : Kunjungi tempat ini, untuk mendapatkan badge ${item.reward}" else ""
                v?.markAsDone?.setOnClickListener {
                    item.let {
                        missionClickListener.onClick(it)
                    }

                }
                val time = (item.createdAt ?: System.currentTimeMillis()).toLong() * 1000
                val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                    time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                ) as String
                v?.tvTimeUpload?.text = niceDateStr
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mission[position].missionItem != null) 1 else 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionListAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingv = ItemMissionBinding.inflate(inflater, parent, false)
        val bindingv2 = ItemSamePlaceBinding.inflate(inflater, parent, false)
        return when (viewType) {
            1 -> MissionListAdapterViewHolder(bindingv, null)
            else -> return MissionListAdapterViewHolder(null, bindingv2)
        }

    }

    override fun onBindViewHolder(holder: MissionListAdapterViewHolder, position: Int) {
        holder.binding(
            mission[position].missionItem,
            mission[position].bottleItem,
            missionClickListener,
            samePlaceClickListener
        )
    }

    override fun getItemCount(): Int = mission.size
}