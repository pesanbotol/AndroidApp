package com.pesanbotol.android.app.ui.search.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pesanbotol.android.app.data.search.model.SearchResult
import com.pesanbotol.android.app.ui.search.fragments.BottleSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.MissionSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.UserSearchFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val searchResult: SearchResult) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = UserSearchFragment()
                val bundle = Bundle()
                bundle.putParcelable(UserSearchFragment.USER_DATA, searchResult.users)
                fragment.arguments = bundle
            }
            1 -> {
                fragment = MissionSearchFragment()
                val bundle = Bundle()
                bundle.putParcelable(MissionSearchFragment.MISSION_DATA, searchResult.mission)
                fragment.arguments = bundle
            }
            2 -> {
                fragment = BottleSearchFragment()
                val bundle = Bundle()
                bundle.putParcelable(BottleSearchFragment.BOTTLE_DATA, searchResult.bottles)
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

}