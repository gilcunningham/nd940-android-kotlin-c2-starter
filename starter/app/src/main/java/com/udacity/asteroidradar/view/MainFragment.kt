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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.AsteroidDatabase
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.model.RefreshAsteroids
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

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

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val dao = AsteroidDatabase.getInstance(requireActivity()).refreshAsteroidsDao()
                val refresh = dao.getRefreshAsteroids()
                println("*** REFRESH $refresh")
                if (refresh != null) {
                    println("*** TEST ${refresh.count }")
                }
                else {
                    println("*** refresh == null")
                }

            }
        }

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
                asteroidAdapter.submitList(asteroids)
            }
        }
    }
}
