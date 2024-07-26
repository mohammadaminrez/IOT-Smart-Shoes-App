package com.mobiledevelopment.smartshoesapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ResultAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ResultAdapter(this)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }

        fetchData()
    }

    private fun fetchData() {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://industrial.api.ubidots.com/api/v2.0/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getData().enqueue(object : Callback<ResponseData> {
            override fun onResponse(
                call: Call<ResponseData>,
                response: Response<ResponseData>
            ) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.results?.let { results ->
                        adapter.updateData(results)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@MainActivity, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "API call failed", t)
            }
        })
    }
}
