package com.songjachin.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.songjachin.weather.ui.place.PlaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //添加fragment
        val placeFragment = PlaceFragment()
        val fm = supportFragmentManager
        val beginTransaction = fm.beginTransaction()
        beginTransaction.add(R.id.container,placeFragment)
        beginTransaction.commit()
    }
}