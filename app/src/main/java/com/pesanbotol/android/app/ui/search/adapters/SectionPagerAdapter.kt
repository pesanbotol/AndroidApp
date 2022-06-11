package com.pesanbotol.android.app.ui.search.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pesanbotol.android.app.ui.search.fragments.BottleSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.MissionSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.UserSearchFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserSearchFragment()
            1 -> fragment = MissionSearchFragment()
            2 -> fragment = BottleSearchFragment()
        }
        return fragment as Fragment
    }

}