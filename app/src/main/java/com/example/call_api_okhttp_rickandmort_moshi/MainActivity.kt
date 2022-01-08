package com.example.call_api_okhttp_rickandmort_moshi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.call_api_okhttp_rickandmort_moshi.databinding.ActivityMainBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okio.IOException



class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val requestFiles = registerForActivityResult(ActivityResultContracts.RequestPermission(), {
            if (it) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                run()
            } else {
                Toast.makeText(applicationContext, "Permission no granted", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.btnFilesPermission.setOnClickListener({
            requestFiles.launch(android.Manifest.permission.INTERNET)
        })


    }


    private val client = OkHttpClient()

    fun run() {
        val moshi: Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

          val request = Request.Builder()
              .url("https://rickandmortyapi.com/api/character/1")
              .build()

          client.newCall(request).enqueue(object : Callback {
              override fun onFailure(call: Call, e: IOException) {
                  e.printStackTrace()
              }

              override fun onResponse(call: Call, response: Response) {
                  response.use {
                      if (!response.isSuccessful) throw IOException("Unexpected code $response")

                      val adapter: JsonAdapter<Character> = moshi.adapter(Character::class.java)
                      val data = adapter.fromJson(response.body!!.source())



                      if (data != null) {
                          Log.i("CHARACTER", data.toString() )
                     }

                  }
              }
          })


    }

}

