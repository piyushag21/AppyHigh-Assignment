package com.example.mynewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.*
import com.littlemango.stacklayoutmanager.StackLayoutManager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var pageNum = 1
    var totalResults = -1
    val TAG = "MainActivity"
    private var articles = mutableListOf<Article>()
    lateinit var adapter: NewsAdapter

    private lateinit var mInterstitialAd: InterstitialAd
    var show : String? = " "


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdClosed() {   // Whenever our ad will close this method will be loaded
                super.onAdClosed()
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        // code for country specific news
        val country = intent.getStringExtra("message_key")
        val final_country = country?.uppercase()
        show = country  // this variable is to store country entered by user for Toast Messages
        var ans = "in" // I have used this variable to store the country entered by user and pass it to url to get country specific news.
        if(final_country == "INDIA")
        {
            ans = "in"
        }
        else
        {
            ans = "us"
        }
        adapter = NewsAdapter(this@MainActivity, articles)
        val recyclerview = findViewById<RecyclerView>(R.id.newsList)
        recyclerview.adapter = adapter

        val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)

        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(3000)

        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener{
            override fun onItemChanged(position: Int) {
                Log.d(TAG,"First Visible item - ${layoutManager.getFirstVisibleItemPosition()}")
                Log.d(TAG,"Total Count - ${layoutManager.itemCount}")
                if(totalResults>layoutManager.itemCount && layoutManager.getFirstVisibleItemPosition() >= layoutManager.itemCount-5){
                    pageNum++
                    getNews(ans)
                }

                var controller = RemoteConfigUtils.getCheckerText()  // this the code for to control ads visibility from firebase remote config
                                                                    // when value of controller is false then ads will become invisible

                if(controller == "true")
                {

                    if(position % 5 == 0 ) // After every 5 news add will be shown
                    {
                        if (mInterstitialAd.isLoaded) {  // This Statement will run when ad is loaded

                            mInterstitialAd.show()
                        }
                        else {
                            Toast.makeText(applicationContext,"Ad cannot be loaded", Toast.LENGTH_LONG).show()
                        }
                    }

                }

            }

        })
        recyclerview.layoutManager = layoutManager

        getNews(ans)

    }

    private fun getNews(ans:String) {
        Log.d(TAG,"Requesting more pages")
        val news = NewsService.newsInstance.getHeadlines(ans,pageNum)
        news.enqueue(object: Callback<News>{

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("piyush","error in feteching code",t)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                Toast.makeText(applicationContext,"Showing News Of $show", Toast.LENGTH_LONG).show()


                val news = response.body()
                if(news != null){
                    Log.d("piyush",news.toString())
                    totalResults = news.totalResults
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }


}