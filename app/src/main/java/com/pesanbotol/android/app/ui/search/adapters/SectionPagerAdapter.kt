package com.pesanbotol.android.app.ui.search.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pesanbotol.android.app.data.search.model.SearchBottlesResponse
import com.pesanbotol.android.app.data.search.model.SearchMissionResponse
import com.pesanbotol.android.app.data.search.model.SearchResult
import com.pesanbotol.android.app.data.search.model.SearchUsersResponse
import com.pesanbotol.android.app.ui.search.fragments.BottleSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.MissionSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.UserSearchFragment

class SectionPagerAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {
    private val fragments: MutableList<Fragment> = mutableListOf(
        UserSearchFragment(),
        MissionSearchFragment(),
        BottleSearchFragment()
    )
    private val searchResult: SearchResult? = null
    override fun getItemCount(): Int {
        return 3
    }

    fun setUsersData(value: SearchUsersResponse) {
        searchResult?.users = value
        (fragments[0] as UserSearchFragment).setData(value)
//        val fragment = UserSearchFragment()
//        val bundle = Bundle()
//        bundle.putParcelable(UserSearchFragment.USER_DATA, searchResult?.users)
//        fragment.arguments = bundle
//        refreshFragment(0, fragment)
//        notifyDataSetChanged()
    }

    fun setMissionsData(value: SearchMissionResponse) {
        searchResult?.mission = value
        (fragments[1] as MissionSearchFragment).setData(value)
//        val fragment = MissionSearchFragment()
//        val bundle = Bundle()
//        bundle.putParcelable(MissionSearchFragment.MISSION_DATA, searchResult?.mission)
//        fragment.arguments = bundle
//        refreshFragment(1, fragment)
//        notifyDataSetChanged()
    }

    fun setBottlesData(value: SearchBottlesResponse) {
        searchResult?.bottles = value
        (fragments[2] as BottleSearchFragment).setData(value)
//        val fragment = BottleSearchFragment()
//        val bundle = Bundle()
//        bundle.putParcelable(BottleSearchFragment.BOTTLE_DATA, searchResult?.bottles)
//        fragment.arguments = bundle
//        refreshFragment(2, fragment)
//        notifyDataSetChanged()

    }

    private fun refreshFragment(index: Int, fragment: Fragment) {
        fragments[index] = fragment
        notifyItemChanged(index)
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = fragments[position]
        return fragment as Fragment
//            0 -> {
//                fragment = fragments[0]
//                val bundle = Bundle()
//                bundle.putParcelable(UserSearchFragment.USER_DATA, searchResult?.users)
//                fragment.arguments = bundle
//            }
//            1 -> {
//                fragment = fragments[1]
//                val bundle = Bundle()
//                bundle.putParcelable(MissionSearchFragment.MISSION_DATA, searchResult?.mission)
//                fragment.arguments = bundle
//            }
//            2 -> {
//                fragment = fragments[2]
//                val bundle = Bundle()
//                bundle.putParcelable(BottleSearchFragment.BOTTLE_DATA, searchResult?.bottles)
//                fragment.arguments = bundle
//            }

    }

}