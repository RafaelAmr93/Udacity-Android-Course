/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        //set the current activity as the lifecycle owner of the binding
        binding.lifecycleOwner = this

        //variavel que faz referencia a propria aplicaçao
        val application = requireNotNull(this.activity).application

        //ao istanciar o bd passando a aplicação, se tem acesso a sua propriedades (o dao)
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        //instancia do viewModelFacory passando a aplicação e o bd com dao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        //como se precisa de parametros para a viewModel, criou-se um viewModelFacory customizado
        //aqui dizemos ao ViewModelProviders para utiliza-lo
        val sleepTrackerViewModel = ViewModelProvider(
                this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        //binding do xml com o viewModel
        binding.sleepTrackerViewModel = sleepTrackerViewModel

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer{ night ->
            night?.let{
                this.findNavController()
                    .navigate(SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightID))
                sleepTrackerViewModel.doneNavigating()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), "Você precisa clicar em Start primeiro", Toast.LENGTH_LONG).show()
            }
        })


        return binding.root
    }
}
