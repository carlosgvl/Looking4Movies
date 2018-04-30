package com.example.carlosvivas.looking4movies.Controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carlosvivas.looking4movies.Model.Movie
import com.example.carlosvivas.looking4movies.R
import kotlinx.android.synthetic.main.item.view.*

import android.R.attr.path
import android.content.Context
import android.widget.Filter
import com.bumptech.glide.Glide
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.URL_IMAGE_W_500
import java.util.ArrayList


/**
 * Created by carlosvivas on 4/28/18.
 */
class MovieAdapter (var MovieItemList: List<Movie>, val clickListener: (Movie) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(MovieItemList[position], clickListener)
    }

    override fun getItemCount() = MovieItemList.size

    fun setSearchResult(result: List<Movie>) {
        MovieItemList = result
        notifyDataSetChanged()
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: Movie, clickListener: (Movie) -> Unit) {
            itemView.tv_title.text = part.title
            itemView.tv_average.text = part.vote_average

            val url = URL_IMAGE_W_500 + part.poster_path
            Glide.with(itemView.context).load(url).into(itemView.iv_movie)

            itemView.setOnClickListener { clickListener(part)}
        }
    }


}