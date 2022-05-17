package com.pesanbotol.android.app.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pesanbotol.android.app.databinding.FragmentAktivitasBinding

class AktivitasFragment : Fragment() {

    private var _binding: FragmentAktivitasBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAktivitasBinding.inflate(inflater, container, false)

        return binding?.root
    }

}