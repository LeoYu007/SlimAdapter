package com.yu1tiao.slimadapter.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yu1tiao.slimadapter.SlimAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        SlimAdapter<Any>().apply {
            register(R.layout.activity_main) { holder, item, position ->

            }
            register(R.layout.activity_main) { holder, item, position ->

            }
            injectorFinder { item, position, itemCount -> R.layout.activity_main }

            itemClickListener { index, item ->

            }
        }
    }
}