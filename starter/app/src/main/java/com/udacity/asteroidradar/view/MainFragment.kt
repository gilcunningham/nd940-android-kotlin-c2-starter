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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment(), MenuProvider {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(requireActivity().application)
        )[MainViewModel::class.java]
    }

    private lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asteroidAdapter = AsteroidAdapter(viewModel.asteroidListClickListener)
        lifecycleScope.launch {
            viewModel.selectedAsteroid.collect { asteroid ->
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
            }
        }
        requireActivity().addMenuProvider(this, this, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.let {
                asteroidAdapter.submitList(it)
            }
        }
        viewModel.filteredAsteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroidAdapter.submitList(asteroids)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            R.id.show_all -> {
                viewModel.showAllAsteroids()
                true
            }

            R.id.show_this_week -> {
                viewModel.showThisWeeksAsteroids()
                true
            }

            R.id.show_today -> {
                viewModel.showTodaysAsteroids()
                true
            }

            else -> false
        }
}
