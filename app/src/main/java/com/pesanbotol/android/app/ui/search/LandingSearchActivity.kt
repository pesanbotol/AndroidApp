package com.pesanbotol.android.app.ui.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.data.search.viewmodel.SearchViewModel
import com.pesanbotol.android.app.databinding.ActivityLandingSearchBinding
import com.pesanbotol.android.app.ui.search.adapters.SectionPagerAdapter
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingSearchBinding
    private val searchViewModel by viewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        searchViewModel.search("")
        searchAnything()
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager2
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabsLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
            searchViewModel.search(binding.etSearch.text.toString())
        }
        searchViewModel.postSearchState.observe(this) { state ->
            when (state) {
                is StateHandler.Loading -> {
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmerAnimation()
                }
                is StateHandler.Success -> {
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    state.data?.let {
                        it.bottles?.let {
                            sectionsPagerAdapter.setBottlesData(it)
                            viewPager.adapter
                        }
                        it.mission?.let {
                            sectionsPagerAdapter.setMissionsData(it)
                        }
                        it.users?.let {
                            sectionsPagerAdapter.setUsersData(it)
                        }
                    }
                }
                is StateHandler.Error -> {
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    Log.e(
                        LandingSearchActivity::class.simpleName,
                        state.message ?: "Unknown error occured"
                    )
                    CommonFunction.showSnackBar(
                        binding!!.root,
                        applicationContext,
                        state.message ?: "Unknown error occured",
                        true
                    )
                }
                else -> {}
            }
        }







    }

    private fun searchAnything() {
        binding.textInputLayout.apply {
            editText?.doOnTextChanged { text, start, before, count -> }
            editText?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    textView: TextView?,
                    actionId: Int,
                    keyEvent: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideSoftKeyboard(textView)
                        try {
                            searchViewModel.search(textView?.text.toString())
                        } catch (ex: Exception) {
                            Toast.makeText(
                                this@LandingSearchActivity,
                                "Failed : $ex",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        return true
                    }
                    return false
                }
            })
        }
    }

    private fun hideSoftKeyboard(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
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