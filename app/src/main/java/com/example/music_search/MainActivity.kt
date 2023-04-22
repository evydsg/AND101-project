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
    var trackURL =""
    var artist = ""
    val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvSpot = binding.songItem
        songList = mutableListOf()
        var query = ""

        createAdapter()
    }

    private fun createAdapter(){
        adapter = SpotAdapter(songList)
        rvSpot.adapter = adapter
        rvSpot.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getSpotDataURL(query: String) {
        val apiKey = getString(R.string.api_key)
        var spotAPIURL = "https://developer.spotify.com/documentation/web-api/reference/search?$query"

        client[spotAPIURL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                var trackArray = json.jsonObject.getJSONObject("tracks").getJSONArray("items")

                for (i in 0 until trackArray.length()) {
                    // $.tracks.items[i].album.images[1].url  images at index 1 are 300x300 index 2 are 64x64
                    songImageURL = trackArray.getJSONObject(i).getJSONObject("album").
                    getJSONArray("images").getJSONObject(1).getString("url")

                    // $.tracks.items[i].name
                    trackName = trackArray.getJSONObject(i).getString("name")

                    // $.tracks.items[i].album.external_urls.spotify
                    trackURL = trackArray.getJSONObject(i).getJSONObject("album").getJSONObject("external_urls").
                    getString("spotify")

                    // $.tracks.items[i].artists[0].name
                    artist = trackArray.getJSONObject(i).getJSONArray("artists").getJSONObject(i).getString("name")

                    songList.add(mutableMapOf(
                    "imageURL" to songImageURL, "trackURL" to trackURL, "trackName" to trackName, "artist" to artist)
                    )
                    Log.d("Spotify API track name", songList.last().toString())
                }

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
}