package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }
    private var viewModelAdapter: AsteroidAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = AsteroidAdapter(::navigateToDetail)

        binding.asteroidRecycler.adapter = viewModelAdapter
        setHasOptionsMenu(true)

        return binding.root
    }


    private fun navigateToDetail(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroid.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.submitList(this)
            }
        })
        viewModel.asteroidsToday.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.submitList(this)
            }
        }
        viewModel.asteroidsWeek.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.submitList(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_rent_menu -> {
                viewModel.getAsteroidsToday()
            }
            R.id.show_all_menu -> {
                viewModel.getAsteroidsWeek()
            }
            R.id.show_buy_menu -> {
                viewModelAdapter?.submitList(viewModel.asteroid.value)
            }
        }
        return true
    }
}
