package io.navendra.retrofitkotlindeferred.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.navendra.retrofitkotlindeferred.R
import io.navendra.retrofitkotlindeferred.service.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = ApiFactory.placeholderApi

        val movieService = ApiFactory.tmdbApi


        //Getting Posts from Jsonplaceholder API
        GlobalScope.launch(Dispatchers.Main) {
            val postRequest = service.getPosts()
            try {
                val response = postRequest.await()
                val posts = response.body()

            }catch (e: Exception){

            }
        }


        //Getting Users from Jsonplaceholder API
        GlobalScope.launch(Dispatchers.Main) {
            val userRequest = service.getUsers()
            try {
                val response = userRequest.await()
                val users = response.body()
                val i=0

            }catch (e: Exception){

            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            val popularMovieRequest = movieService.getPopularMovie()
            try {
                val response = popularMovieRequest.await()
                val movieResponse = response.body() //This is single object Tmdb Movie response
                val popularMovies = movieResponse?.results // This is list of TMDB Movie

            }catch (e: Exception){

            }
        }

    }
}
