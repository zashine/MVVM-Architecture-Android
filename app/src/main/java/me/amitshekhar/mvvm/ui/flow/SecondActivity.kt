package me.amitshekhar.mvvm.ui.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.amitshekhar.mvvm.databinding.ActivitySecondBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

@OptIn(DelicateCoroutinesApi::class)
class SecondActivity : AppCompatActivity() {

    private val TAG = "SecondActivity"

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)

        GlobalScope.launch {
            /*val comments = api.getComments().await()
            for (comment in comments) {
                Log.d(TAG, comment.toString())
            }*/
            val response = api.getComments().awaitResponse()
            if (response.isSuccessful) {
                response.body()?.let {
                    for (comment in it) {
                        Log.d(TAG, comment.toString())
                    }
                }
            }
        }

        GlobalScope.launch {
            val response = api.getComments2()
            if (response.isSuccessful) {
                response.body()?.let {
                    for (comment in it) {
                        Log.d(TAG, comment.toString())
                    }
                }
            }
        }
    }

    private fun normalRequest(api: MyApi) {
        api.getComments().enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (comment in it) {
                            Log.d(TAG, comment.toString())
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Comment>>, p1: Throwable) {
                Log.e(TAG, "Error: $p1")
            }
        })
    }
}