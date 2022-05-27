package com.pesanbotol.android.app.ui.followers_following

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayoutMediator
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.databinding.ActivityFollowBinding
import com.pesanbotol.android.app.ui.followers_following.adapter.SectionPagerAdapter

class FollowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowBinding
    private lateinit var sectionPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
//            viewPager.isUserInputEnabled = true
        }.attach()
    }

    companion object{
        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following,
        )
    }

}