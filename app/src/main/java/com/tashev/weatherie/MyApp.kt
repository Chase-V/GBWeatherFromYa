package com.tashev.weatherie

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.tashev.weatherie.repository.room.HistoryDAO
import com.tashev.weatherie.repository.room.HistoryDataBase
import okhttp3.internal.Internal.instance

class MyApp:Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        val context: Context = MyApp.applicationContext()
    }

    companion object{
        private var appInstance:MyApp? = null
        private var db:HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        private var instance: MyApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun getHistoryDAO():HistoryDAO{
            if (db == null){
                if (appInstance!=null){
                    db = Room.databaseBuilder(appInstance!!.applicationContext, HistoryDataBase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()
                } else throw IllegalStateException("appInstance==null")
            }
            return db!!.historyDAO()
        }

        fun getStringResourceByName(aString: String): String {
            var aStringChanged = aString
            if (aString.contains("-", true)) {
                aStringChanged = aString.replace("-", "_")
            }
            val packageName = applicationContext().resources.getResourcePackageName(R.string.clear)
            val resId = applicationContext().resources.getIdentifier(aStringChanged, "string", packageName)
            return applicationContext().getString(resId)
        }

    }

}