package com.example.mynewsapp

import android.content.Intent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://newsapi.org/v2/top-headlines?country=us&apiKey=1f9f17d2bab84808907581288396abdb

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "1f9f17d2bab84808907581288396abdb"


interface NewsInterface{
    @GET("v2/top-headlines?apiKey=$API_KEY")  // Retrogit 2 used for GET method which is used to request data from a specified source.
    fun getHeadlines(@Query("country")country: String, @Query("page")page : Int) :Call<News>
}

object NewsService{
    val newsInstance: NewsInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(NewsInterface::class.java)
    }
}
