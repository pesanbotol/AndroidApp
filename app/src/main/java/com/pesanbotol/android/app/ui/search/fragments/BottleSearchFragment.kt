package com.pesanbotol.android.app.ui.search.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.search.model.BottleItems
import com.pesanbotol.android.app.data.search.model.SearchBottlesResponse
import com.pesanbotol.android.app.databinding.FragmentBottleSearchBinding
import com.pesanbotol.android.app.databinding.FragmentHomeBinding
import com.pesanbotol.android.app.ui.search.adapters.BottleListAdapter
import com.pesanbotol.android.app.ui.search.interfaces.BottleItemClickListener

class BottleSearchFragment : Fragment(), BottleItemClickListener {
    private var _binding: FragmentBottleSearchBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBottleSearchBinding.inflate(inflater, container, false)
        _binding?.rvBottles?.layoutManager = LinearLayoutManager(requireContext())
        val arg = arguments
        arg?.getParcelable<SearchBottlesResponse>(BOTTLE_DATA)?.let {
            _binding?.rvBottles?.adapter = BottleListAdapter(ArrayList(it.hits!!), this)
        }

        return _binding!!.root
    }

    companion object {
        const val BOTTLE_DATA = "BOTTLE_DATA"
    }

    override fun onClick(item: BottleItems) {

    }
}