package com.example.music_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.example.music_search.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var songList: MutableList<MutableMap<String, String>>
    private lateinit var rvSpot: RecyclerView
    private lateinit var adapter: SpotAdapter

    var songImageURL = ""
    var trackName = ""
    var trackURL
    val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvSpot = binding.songItem
        songList = mutableListOf()
    }

    private fun createAdapter(genRange: Pair<Int, Int>){
        adapter = SpotAdapter(songList)
        rvSpot.adapter = adapter
        rvSpot.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getPokeDataURL() {
        val client = AsyncHttpClient()
        var baseImageURL = "https://pokeapi.co/api/v2/pokemon/"

        var randInt = Random.nextInt(1,150)
        var pokeURL = baseImageURL + randInt.toString()
//        Log.d("Random Int Var", randInt.toString())
//        Log.d("Random Int Var", pokeURL)

        client[pokeURL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                songImageURL = ""

                trackName = json.jsonObject.getString("").replaceFirstChar { it.titlecase() }
                Log.d("Spotify API", "response successful$json")
                Log.d("Spotify API track name", trackName)

                // get first ability
                trackURL = json.jsonObject.get
                Log.d("Spotify API ability", trackURL)

                songList.add(mutableMapOf("image" to songImageURL, "songLink" to trackURL, "songName" to trackName))
//                Log.d("Spotify API list", songList.toString())

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Spotify API Error", errorResponse)
            }
        }]

}