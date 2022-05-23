package com.pesanbotol.android.app.data.dummy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.dummy.model_dummy.Comment
import com.pesanbotol.android.app.databinding.ItemCommentsBinding

class CommentUserAdapter(private val dataComment: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentUserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (avatar, name, desc, date) = dataComment[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .error(R.mipmap.ic_launcher)
            .into(holder.binding.avatarUser)
        holder.binding.tvName.text = name
        holder.binding.tvDescription.text = desc
        holder.binding.tvDates.text = date
    }

    override fun getItemCount(): Int {
        return dataComment.size
    }

    class ListViewHolder(val binding: ItemCommentsBinding) : RecyclerView.ViewHolder(binding.root)
}
