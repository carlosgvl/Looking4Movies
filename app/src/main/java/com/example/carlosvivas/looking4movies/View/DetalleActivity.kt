package com.example.carlosvivas.looking4movies.View

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.carlosvivas.looking4movies.R
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans

import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.content_detalle.*
import kotlinx.android.synthetic.main.item.view.*
import java.text.SimpleDateFormat

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle("Movies")



        tv_title_detalle.text = intent.getStringExtra("title")
        val url = Connectionconstans.URL_IMAGE_W_500 + intent.getStringExtra("poster_path")
        Glide.with(this).load(url).into(iv_detalle)
        tv_release_date.text = intent.getStringExtra("release_date")
        tv_vote_average.text = intent.getStringExtra("vote_average")
        tv_vote_count.text = intent.getStringExtra("vote_count")
        tv_overview.text = intent.getStringExtra("overview")


    }

    override fun onWindowFocusChanged(hasFocus:Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
        {
            window.decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
