package com.tashev.gbweatherfromya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tashev.gbweatherfromya.view.SomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.main_container, SomeFragment.newInstance()).commit()
    }
}