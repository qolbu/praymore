package com.doa.terus

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.doa.terus.db.DatabaseHelper
import com.doa.terus.experimental.Android
import com.doa.terus.network.ShalatClient
import com.doa.terus.home.HomeActivity
import kotlinx.coroutines.launch


class SplashScreenActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val databaseHelper by lazy {
        DatabaseHelper(
            context = this@SplashScreenActivity,
            name = DatabaseHelper.DATABASE_NAME,
            factory = null,
            version = DatabaseHelper.DATABASE_VERSION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        doLoadData()
    }

    /**
     * @description Load data from API and save it to local database
     */
    private fun doLoadData() {
        launch(Android) {
            val itemCountDataCityLocal = databaseHelper.countDataCity()
            val resultDataKota = ShalatClient.getCityData().await()
            val intentHomeActivity = Intent(this@SplashScreenActivity, HomeActivity::class.java)
            intentHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            if (itemCountDataCityLocal == resultDataKota.count) {
                startActivity(intentHomeActivity)
            } else {
                databaseHelper.deleteDataCity()
                databaseHelper.insertDataCity(resultDataKota.data)
                startActivity(intentHomeActivity)
            }
        }
    }
}