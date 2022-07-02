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

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        //job opera sobre coroutines (?)
        private var viewModelJob = Job()

        override fun onCleared() {
                super.onCleared()
                viewModelJob.cancel()
        }

        //definindo o scopo de UI
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        //linha mais atual da tabela daily_sleep_quality_table
        private var tonight = MutableLiveData<SleepNight?>()

        //todas as noites, chamada de SleepDatabaseDAO
        val nights = database.getAllNights()

        val nightsStrings = Transformations.map(nights) { nights ->
                formatNights(nights, application.resources)
        }


        //inicializa tonight com a linha mais recente, precisa ser live data para atualizar auto
        init {
            initializeTonight()
        }

        //funcao que começa uma noite ao tocar Start feita pela coroutine do escopo de UI
        private fun initializeTonight(){
               viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

        //funcao que chama uma coroutine para registrar o inicio da noite pelo escopo de IO
        private suspend fun getTonightFromDatabase(): SleepNight?{
                return withContext(Dispatchers.IO){
                        var night = database.getTonight()
                        if (night?.endTimeMilli != night?.startTimeMilli){
                                night = null
                        }
                        night
                }
        }


        //lauch inicia a coroutine no escopo que a chama
        fun onStartTracking(){
                uiScope.launch {
                        val newNight = SleepNight()
                        insert(newNight)
                        Log.i("onStartTracking ID", "${newNight.nightID}")
                        tonight.value = newNight
               }
        }

        private suspend fun insert(night: SleepNight){
                withContext(Dispatchers.IO){
                        database.insert(night)
                        Log.i("oninsert ID", "${night.nightID}")
                }
        }

        private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
        val navigateToSleepQuality: LiveData<SleepNight>
                get() = _navigateToSleepQuality

        fun doneNavigating(){
                _navigateToSleepQuality.value = null
        }

        fun onStopTracking(){
                uiScope.launch {
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilli = System.currentTimeMillis()
                        update(oldNight)
                        _navigateToSleepQuality.value = oldNight
                }
        }

        private suspend fun update(night: SleepNight){
                withContext(Dispatchers.IO){
                        database.update(night)
                        Log.i("onupdate ID", "${night.nightID}")
                }
        }

        fun onClear(){
                uiScope.launch {
                        clear()
                        tonight.value = null
                }
        }

        private suspend fun clear(){
                withContext(Dispatchers.IO){
                        database.clear()
                }
        }



}

