package com.tashev.gbweatherfromya.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tashev.gbweatherfromya.R
import com.tashev.gbweatherfromya.view.citiesList.CitiesListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState==null)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, CitiesListFragment.newInstance()).commit()
    }
}