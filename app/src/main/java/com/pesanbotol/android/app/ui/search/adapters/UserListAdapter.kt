package com.pesanbotol.android.app.ui.search.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.search.model.UserItems
import com.pesanbotol.android.app.databinding.ItemSamePlaceBinding
import com.pesanbotol.android.app.ui.search.interfaces.UserItemClickListener

class UserListAdapter(
    private val users: ArrayList<UserItems?>,
    private val clickListener: UserItemClickListener
) :
    RecyclerView.Adapter<UserListAdapter.MissionListAdapterViewHolder>() {
    class MissionListAdapterViewHolder(
        private val v2: ItemSamePlaceBinding
    ) :
        RecyclerView.ViewHolder(v2.root) {
        fun binding(
            user: UserItems?,
            clickListener: UserItemClickListener
        ) {
            v2.imageDialogProfile.visibility = View.GONE
//            Glide.with(v2!!.root).load(user?.document?.displayName)
//                .apply(
//                    RequestOptions().error(
//                        ContextCompat.getDrawable(
//                            v2.root.context,
//                            R.drawable.empty_profile,
//                        ),
//                    )
//                )
//                .into(v2.imageDialogProfile)
            v2.titleUsername.text = user?.document?.displayName ?: user?.document?.username ?: "-"
            v2.snippetDesc.text = user?.document?.description ?: ""
            v2.tvTimeUpload.text = ""
            v2.root.setOnClickListener {
                user?.apply {
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
            users[position],
            clickListener
        )
    }

    override fun getItemCount(): Int = users.size
}