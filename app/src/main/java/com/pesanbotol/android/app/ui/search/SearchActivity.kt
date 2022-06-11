package com.pesanbotol.android.app.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.data.search.model.UserItems
import com.pesanbotol.android.app.data.search.viewmodel.SearchViewModel
import com.pesanbotol.android.app.databinding.ActivitySearchBinding
import com.pesanbotol.android.app.ui.search.adapters.UserListAdapter
import com.pesanbotol.android.app.ui.search.interfaces.UserItemClickListener
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), UserItemClickListener {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding

    private val searchViewModel by viewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        searchAnything()
        _binding?.listItem?.layoutManager = LinearLayoutManager(this)
        searchViewModel.postSearchState.observe(this) {
            when (it) {
                is StateHandler.Loading -> {
                    _binding?.progressBar?.visibility = View.VISIBLE
                }
                is StateHandler.Success -> {
                    _binding?.progressBar?.visibility = View.GONE
                    _binding?.let { view ->
                        println("RESULT ${it.data?.users?.hits?.size}")
                        it.data?.users?.hits?.let { items ->
                            view.listItem.adapter = UserListAdapter(ArrayList(items), this)

                        }

                    }
                }
                is StateHandler.Error -> {
                    _binding?.progressBar?.visibility = View.GONE

                    Log.e(SearchActivity::class.simpleName, it.message ?: "Unknown error occured")
                    CommonFunction.showSnackBar(
                        _binding!!.root,
                        applicationContext,
                        it.message ?: "Unknown error occured",
                        true
                    )
                }
            }
        }
    }

    private fun searchAnything() {
        binding?.textInputLayout?.apply {
            editText?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    textView: TextView?,
                    actionId: Int,
                    keyEvent: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideSoftKeyboard(textView)
                        // result search in view model here
                        // searchCourseViewModel.searchCourseByKeyword(v.text.trim().toString())
                        try {
                            searchViewModel.search(textView?.text.toString())
                            Toast.makeText(
                                this@SearchActivity,
                                "Search ADA $textView ",
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (ex: Exception) {
                            Toast.makeText(this@SearchActivity, "Search $ex", Toast.LENGTH_LONG)
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

    override fun onClick(user: UserItems) {

    }
}