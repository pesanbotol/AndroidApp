package com.pesanbotol.android.app.ui.search.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.data.search.model.MissionItems
import com.pesanbotol.android.app.data.search.model.SearchMissionResponse
import com.pesanbotol.android.app.databinding.FragmentMissionSearchBinding
import com.pesanbotol.android.app.ui.search.adapters.MissionListAdapter
import com.pesanbotol.android.app.ui.search.interfaces.MissionItemClickListener

class MissionSearchFragment : Fragment(), MissionItemClickListener {
    private var _binding: FragmentMissionSearchBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMissionSearchBinding.inflate(inflater, container, false)
        _binding?.rvMission?.layoutManager = LinearLayoutManager(requireContext())
        val arg = arguments
        arg?.getParcelable<SearchMissionResponse>(MISSION_DATA)?.let {
            _binding?.rvMission?.adapter = MissionListAdapter(ArrayList(it.hits!!), this)
        }
        return _binding!!.root
    }

    companion object {
        const val MISSION_DATA = "MISSION_DATA"
    }

    override fun onClick(item: MissionItems) {
    }
}