package com.pesanbotol.android.app.ui.search_bottle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.databinding.ActivitySearchBinding
import com.pesanbotol.android.app.databinding.FragmentHomeBinding

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        searchAnything()
    }

    private fun searchAnything(){
        binding?.textInputLayout?.apply {
            editText?.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        hideSoftKeyboard(textView)
                        // result search in view model here
                        // searchCourseViewModel.searchCourseByKeyword(v.text.trim().toString())
                        return true
                    }
                    return false
                }
            })
        }
    }

    private fun hideSoftKeyboard(view: View?){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}