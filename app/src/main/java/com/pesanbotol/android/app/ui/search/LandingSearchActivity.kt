package com.pesanbotol.android.app.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.databinding.ActivityLandingSearchBinding
import com.pesanbotol.android.app.ui.search.adapters.SectionPagerAdapter

class LandingSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager2
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabsLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    companion object {
        @StringRes
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_user,
            R.string.tab_text_mission,
            R.string.tab_text_bottle,
        )
    }

}