package com.tashev.weatherie

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.commit
import com.tashev.weatherie.view.citiesList.CitiesListFragment
import com.tashev.weatherie.view.contentProvider.ContentProviderFragment
import com.tashev.weatherie.view.history.HistoryFragment

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            val mainContainer: FrameLayout = findViewById(R.id.main_container)

            when (applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    mainContainer.background =
                        AppCompatResources.getDrawable(applicationContext, R.drawable.bg_night)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    mainContainer.background =
                        AppCompatResources.getDrawable(applicationContext, R.drawable.bg_day)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    mainContainer.background =
                        AppCompatResources.getDrawable(applicationContext, R.drawable.bg_day)
                }
            }

        if (savedInstanceState == null)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.main_container, CitiesListFragment.newInstance())
            }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_history ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, HistoryFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }
            R.id.option_provider ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, ContentProviderFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}