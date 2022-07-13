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
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://mars.udacity.com/"
enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"), SHOW_BUY("buy"), SHOW_ALL("all") }

//transforma a resposta json em objetos kotlin
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//cria o objeto retrofit para fazer requisições http
private val retrofit = Retrofit.Builder()
        //adiciona ao retrofit a conversao de json p/ string
        .addConverterFactory(MoshiConverterFactory.create(moshi))
       .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()


interface MarsApiService{
    @GET("realestate")
    suspend fun getProperties(@Query("filter") type: String) : List<MarsProperty>
    //An invocation of a Retrofit method that sends a request to a webserver and returns a response.
    // Each call yields its own HTTP request and response pair.
}

//torna a interface acessível a todo app
object MarsApi{
    val retrofitService : MarsApiService by lazy{
        retrofit.create(MarsApiService::class.java)
    }
}

