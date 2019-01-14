# RetrofitKotlinDeferred
Simple to Complex Tutorial for making network calls in Android using Retrofit2, Kotlin and its Deferred Type

[For detailed explanation of code, visit here.](https://medium.com/@navendra/android-networking-in-2019-retrofit-with-kotlins-coroutines-aefe82c4d777)

### Index
* [Dependencies](https://github.com/navi25/RetrofitKotlinDeferred#dependencies) 
* [API #1 : JSONPlaceHolder API](https://github.com/navi25/RetrofitKotlinDeferred#api-1--jsonplaceholder-api)
  * [A simple Example (Fetch posts)](https://github.com/navi25/RetrofitKotlinDeferred#a-simple-example)
* [API #2: TMDB API](https://github.com/navi25/RetrofitKotlinDeferred#api-2--tmdb-api)
  * [Hiding API key in Version Control System](https://github.com/navi25/RetrofitKotlinDeferred#hiding-api-key-in-version-control-optional-but-recommended)
  * [A Simple Example (Fetching popuplar movies)](https://github.com/navi25/RetrofitKotlinDeferred#a-simple-example-1)
* [Example Project](https://github.com/navi25/RetrofitKotlinDeferred#example-project)

### Dependencies

Add the following to the App dependencies

```kotlin
// build.gradle(Module: app)
dependencies {

    def moshiVersion="1.8.0"
    def retrofit2_version = "2.5.0"
    def okhttp3_version = "3.12.0"
    def kotlinCoroutineVersion = "1.0.1"

     
    //Moshi
    implementation "com.squareup.moshi:moshi-kotlin:$moshiVersion"
    kapt "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"

    //Retrofit2
    implementation "com.squareup.retrofit2:retrofit:$retrofit2_version"
    implementation "com.squareup.retrofit2:converter-moshi:$retrofit2_version"
    implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"

    //Okhttp3
    implementation "com.squareup.okhttp3:okhttp:$okhttp3_version"
    implementation 'com.squareup.okhttp3:logging-interceptor:3.11.0'

    //Kotlin Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutineVersion"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion"

    //Picasso for Image Loading
    implementation ('com.squareup.picasso:picasso:2.71828'){
        exclude group: "com.android.support"
    }
}
```

## [API #1 : JSONPlaceHolder API](https://jsonplaceholder.typicode.com ) 
A Fake Online REST API for Testing and Prototyping

JSONPlaceholder comes with a set of 6 common resources:

* [/posts](https://jsonplaceholder.typicode.com/posts)	100 posts
* [/comments](https://jsonplaceholder.typicode.com/comments)	500 comments
* [/albums](https://jsonplaceholder.typicode.com/albums)	100 albums
* [/photos](https://jsonplaceholder.typicode.com/photos)	5000 photos
* [/todos](https://jsonplaceholder.typicode.com/todos)	200 todos
* [/users](https://jsonplaceholder.typicode.com/users)	10 user



### A simple example

**Global Setup**
```kotlin

//ApiFactory to create jsonPlaceHolder Api
object Apifactory{
    fun retrofit() : Retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()   

    val placeHolderApi : PlaceholderApi = retrofit().create(PlaceholderApi::class.java)
} 

```

**Fetching Posts from /posts**

```kotlin

//Data Model for Post
data class PlaceholderPosts(
    val id: Int,
    val userId : Int,
    val title: String,
    val body: String
)

//A retrofit Network Interface for the Api
interface PlaceholderApi{
    @GET("/posts")
    fun getPosts() : Deferred<Response<List<PlaceholderPosts>>>
}

//Finally making the call
val service = ApiFactory.placeholderApi
GlobalScope.launch(Dispatchers.Main) {
    val postRequest = service.getPosts() // Making Network call
    try {
        val response = postRequest.await()
        val posts = response.body() // This is List<PlaceholderPosts> 
    }catch (e: Exception){

    }
}
```

**Fetching Users from /users**

Data Model for user
```kotlin

// Data model for user
data class PlaceholderUsers(
    val id:Int,
    val name: String,
    val username: String,
    val email: String,
    val address: PlaceholderAddress,
    val phone: String,
    val website: String,
    val company: PlaceholderCompany
)

/**
 * Helper data classes for PlaceholderUsers
 */
data class PlaceholderAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: PlaceholderGeo
)

data class PlaceholderGeo(
    val lat: String,
    val lng: String
)

data class PlaceholderCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

```

Api call for User

```kotlin

interface PlaceholderApi{
    @GET("/posts")
    fun getPosts() : Deferred<Response<List<PlaceholderPosts>>>

    @GET("/users")
    fun getUsers() : Deferred<Response<List<PlaceholderUsers>>>
}

//Getting Users from Jsonplaceholder API
GlobalScope.launch(Dispatchers.Main) {
    val userRequest = service.getUsers()
    try {
        val response = userRequest.await()
        val users = response.body() // This is List<PlaceholderUsers>

    }catch (e: Exception){

    }
}

```

## [API #2 : TMDB API](https://developers.themoviedb.org/3) 

The Movie Database (TMDb) API - It contains list of all popular, upcoming, latest, now showing etc movie and tv shows. This is one of the most popular API to play with too.

TMDB api requires an Api key to make requests. 

* Make an account at [TMDB](https://www.themoviedb.org/)  
* [Follow steps described here to register for an Api key](https://developers.themoviedb.org/3/getting-started/introduction). 

### Hiding API key in Version Control (Optional but Recommended)

Once you have Api key, do the following steps to hide it in VCS.

* Add your key in **local.properties** present in root folder.
* Get access to the key in **build.gradle** programmatically.
* Then the key is available to you in the program though **BuildConfig**.

```kotlin
//In local.properties
tmdb_api_key = "xxxxxxxxxxxxxxxxxxxxxxxxxx"

//In build.gradle (Module: app)
buildTypes {
    Properties properties = new Properties()
    properties.load(project.rootProject.file("local.properties").newDataInputStream())
    def tmdbApiKey = properties.getProperty("tmdb_api_key", "")

    debug {
        buildConfigField 'String', "TMDB_API_KEY", tmdbApiKey
        resValue 'string', "api_key", tmdbApiKey
    }

    release {
        buildConfigField 'String', "TMDB_API_KEY", tmdbApiKey
        resValue 'string', "api_key", tmdbApiKey
    }
}

//In your Constants File
var tmdbApiKey = BuildConfig.TMDB_API_KEY

```

### A Simple Example

#### Global Setup

```kotlin

//ApiFactory to create jsonPlaceHolder Api
object Apifactory{
    fun retrofit() : Retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()   

   val tmdbApi : TmdbApi = retrofit().create(TmdbApi::class.java)
} 

```

### Fetching Popular movies from Endpoint : /movie/popular

```kotlin
// Data Model for TMDB Movie item
data class TmdbMovie(
    val id: Int,
    val vote_average: Double,
    val title: String,
    val overview: String,
    val adult: Boolean
)

// Data Model for the Response returned from the TMDB Api
data class TmdbMovieResponse(
    val results: List<TmdbMovie>
)

//A retrofit Network Interface for the Api
interface TmdbApi{
    @GET("movie/popular")
    fun getPopularMovie(): Deferred<Response<TmdbMovieResponse>>
}

//Finally making the call
val movieService = ApiFactory.tmdbApi
GlobalScope.launch(Dispatchers.Main) {
    val popularMovieRequest = movieService.getPopularMovie()
    try {
        val response = popularMovieRequest.await()
        val movieResponse = response.body() //This is single object Tmdb Movie response
        val popularMovies = movieResponse?.results // This is list of TMDB Movie
    }catch (e: Exception){

    }
}
```

## Example Project

This project is filled with these examples. Clone it, add your API key in the local.properties and check out the implementation.

Happy Coding!
