package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment(), MenuProvider {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().addMenuProvider(this, this, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater).apply {
        lifecycleOwner = this@MainFragment
        viewModel = this@MainFragment.viewModel
    }.root

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asteroid.observe(viewLifecycleOwner) { asteroids ->
            asteroids.forEach {
                println("** ${it.codename}")
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        println("*** on menu")
        return true
    }
}
