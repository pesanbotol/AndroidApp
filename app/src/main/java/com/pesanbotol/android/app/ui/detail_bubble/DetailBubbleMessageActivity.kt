package com.pesanbotol.android.app.ui.detail_bubble

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.dummy.model_dummy.Comment
import com.pesanbotol.android.app.data.dummy.adapter.CommentUserAdapter
import com.pesanbotol.android.app.databinding.ActivityDetailBubbleMessageBinding

class DetailBubbleMessageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBubbleMessageBinding

    private var dataUser = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBubbleMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvComments.setHasFixedSize(true)
        binding.btnAddComments.setOnClickListener(this)

        dataUser.addAll(listComment)
        showRecycleView()
    }

    private val listComment: ArrayList<Comment>
        get() {
            val dataPhoto = resources.getStringArray(R.array.avatar)
            val dataName = resources.getStringArray(R.array.name)
            val dataDesc = resources.getStringArray(R.array.description)
            val dataDate = resources.getStringArray(R.array.dates)
            val listComment = ArrayList<Comment>()
            for (i in dataName.indices) {
                val users = Comment(
                    dataPhoto[i],
                    dataName[i],
                    dataDesc[i],
                    dataDate[i],
                )
                listComment.add(users)
            }
            return listComment
        }

    private fun showRecycleView() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvComments.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvComments.layoutManager = LinearLayoutManager(this)
        }
        val listAdapter = CommentUserAdapter(dataUser)
        binding.rvComments.adapter = listAdapter
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_add_comments -> {
            }
        }
    }
}