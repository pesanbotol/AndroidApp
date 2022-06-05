package com.pesanbotol.android.app.ui.detail_bubble

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.dummy.Comment
import com.pesanbotol.android.app.databinding.ActivityDetailBubbleMessageBinding
import java.util.*


class DetailBubbleMessageActivity : AppCompatActivity()
//    , View.OnClickListener
{
    private lateinit var binding: ActivityDetailBubbleMessageBinding

    private var dataUser = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBubbleMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var bubble = intent.getParcelableExtra<BottleItem>("bubble")
        bubble?.let {
            Glide.with(this).load(it.contentImage?.mediaThumbnailUrl).into(binding.imageView2)
            binding.tvName.text = it.user?.username ?: "unknown"
            binding.tvDescription.text = it.contentText
            if (it.geo?.get(0) != null && it.geo[1] != null) {
                it.geo.let { geo ->
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> =
                            geocoder.getFromLocation(geo[0]!!, geo[1]!!, 1)
//                    val address: String = addresses[0].getAddressLine(0)
                    val city: String = addresses[0].locality ?: ""
                    val state: String = addresses[0].adminArea ?: ""
//                    val zip: String = addresses[0].postalCode
                    val country: String = addresses[0].countryName ?: ""
                    binding.tvCity.text = "$city, $state - $country"
                }
            }


        }

//        binding.rvComments.setHasFixedSize(true)
//        binding.btnAddComments.setOnClickListener(this)

//        dataUser.addAll(listComment)
//        showRecycleView()
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

//    private fun showRecycleView() {
//        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            binding.rvComments.layoutManager = GridLayoutManager(this, 2)
//        } else {
//            binding.rvComments.layoutManager = LinearLayoutManager(this)
//        }
//        val listAdapter = CommentUserAdapter(dataUser)
//        binding.rvComments.adapter = listAdapter
//    }

//    override fun onClick(view: View?) {
//        when(view?.id){
//            R.id.btn_add_comments -> {
//                val intent = Intent(this, CommentBottleActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
}