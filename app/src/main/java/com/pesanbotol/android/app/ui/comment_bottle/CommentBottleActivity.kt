package com.pesanbotol.android.app.ui.comment_bottle

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.dummy.Comment
import com.pesanbotol.android.app.data.dummy.CommentUserAdapter
import com.pesanbotol.android.app.databinding.ActivityCommentBottleBinding

class CommentBottleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBottleBinding
    private var dataComment = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBottleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvAllComments.setHasFixedSize(true)

        dataComment.addAll(listComment)
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
            binding.rvAllComments.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvAllComments.layoutManager = LinearLayoutManager(this)
        }
        val listAdapter = CommentUserAdapter(dataComment)
        binding.rvAllComments.adapter = listAdapter
    }
}