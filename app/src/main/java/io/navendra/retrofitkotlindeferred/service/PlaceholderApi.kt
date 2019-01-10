package io.navendra.retrofitkotlindeferred.service

import io.navendra.retrofitkotlindeferred.data.PlaceholderPosts
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface PlaceholderApi{

    @GET("/posts")
    fun getPosts() : Deferred<Response<List<PlaceholderPosts>>>

}