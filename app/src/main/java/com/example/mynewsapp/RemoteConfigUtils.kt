package com.example.mynewsapp

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtils {

    private const val TAG = "RemoteConfigUtils"

    private const val checker = "check"  // here check is the Parameter name(key) used in Firebase for remotte conifg and checker is varible holding it.

    private val DEFAULTS: HashMap<String, Any> =  // Hash map is used to Map cheker to true
        hashMapOf(
            checker to "true"
        )


    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init() {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {


        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            if (BuildConfig.DEBUG) {
                minimumFetchIntervalInSeconds = 0
            } else {
                minimumFetchIntervalInSeconds = 60 * 60
            }
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(DEFAULTS)

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            Log.d(TAG, "addOnCompleteListener")
        }

        return remoteConfig
    }

    fun getCheckerText(): String = remoteConfig.getString(checker)  // this function is called in our MainActivity to get value of checker

}