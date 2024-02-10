package com.udacity.asteroidradar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.MainViewModel

class MainFragment : Fragment(), MenuProvider {

    //private val viewModel : MainViewModel by viewModels()
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(requireActivity().application)
        )[MainViewModel::class.java]
    }

    private lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().addMenuProvider(this, this, Lifecycle.State.RESUMED)
        asteroidAdapter = AsteroidAdapter(viewModel.asteroidListClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater).apply {
        lifecycleOwner = this@MainFragment
        viewModel = this@MainFragment.viewModel
        asteroidRecycler.layoutManager = LinearLayoutManager(this@MainFragment.context)
        asteroidRecycler.adapter = asteroidAdapter
    }.root

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.let {
                asteroidAdapter.submitList(asteroids)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        return true
    }
}
