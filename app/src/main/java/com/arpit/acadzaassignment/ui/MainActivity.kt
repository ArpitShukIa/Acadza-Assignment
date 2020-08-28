package com.arpit.acadzaassignment.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.arpit.acadzaassignment.R
import com.arpit.acadzaassignment.databinding.ActivityMainBinding
import com.arpit.acadzaassignment.viewmodels.HomeViewModel
import com.arpit.acadzaassignment.adapters.PlaylistAdapter
import com.arpit.acadzaassignment.util.getViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels { getViewModelFactory() }

    private lateinit var binding: ActivityMainBinding
    private var lastQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.playlists.adapter = PlaylistAdapter()

        val animator = binding.playlists.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false
    }

    fun loadSavedPlaylists(v: View) {
        lastQuery = ""
        viewModel.loadSavedPlaylists()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != lastQuery) {
                    lastQuery = query
                    viewModel.loadPlaylists(query)
                    searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onResume() {
        super.onResume()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}