package com.pesanbotol.android.app.ui.search.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.search.model.SearchMissionResponse
import com.pesanbotol.android.app.data.search.model.SearchUsersResponse
import com.pesanbotol.android.app.data.search.model.UserItems
import com.pesanbotol.android.app.databinding.FragmentMissionSearchBinding
import com.pesanbotol.android.app.databinding.FragmentUserSearchBinding
import com.pesanbotol.android.app.ui.detail_users.DetailUsersActivity
import com.pesanbotol.android.app.ui.search.adapters.MissionListAdapter
import com.pesanbotol.android.app.ui.search.adapters.UserListAdapter
import com.pesanbotol.android.app.ui.search.interfaces.UserItemClickListener

class UserSearchFragment : Fragment(), UserItemClickListener {
    private var _binding: FragmentUserSearchBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        _binding?.rvUsers?.layoutManager = LinearLayoutManager(requireContext())
        val arg = arguments
        arg?.getParcelable<SearchUsersResponse>(USER_DATA)?.let {
            _binding?.rvUsers?.adapter = UserListAdapter(ArrayList(it.hits!!), this)
        }
        return _binding!!.root
    }

    fun setData(data: SearchUsersResponse) {
        _binding?.rvUsers?.adapter = UserListAdapter(ArrayList(data.hits!!), this)
    }

    companion object {
        const val USER_DATA = "USER_SEARCH"
    }

    override fun onClick(user: UserItems) {
        user.document?.id?.let {
            val intent = Intent(requireActivity(), DetailUsersActivity::class.java)
            intent.putExtra(DetailUsersActivity.UID_DATA, it)
            startActivity(intent)
        }
    }
}