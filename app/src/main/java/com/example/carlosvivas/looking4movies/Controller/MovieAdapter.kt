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
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Filter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.example.carlosvivas.looking4movies.Model.MovieSq
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.URL_IMAGE_W_500
import kotlinx.android.synthetic.main.item.*

import com.example.carlosvivas.looking4movies.R.id.progressBar

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.io.ByteArrayOutputStream
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


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

    fun imageViewToByte(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
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



            /*     val bitmap = (itemView.iv_movie.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()



            val usersDBHelper = MoviesDBHelper(itemView.context)
            var result = usersDBHelper.insertUser(MovieSq(  vote_count = part.vote_count, id = part.id,video =  part.video,vote_average =  part.vote_average,title =  part.title,popularity =  part.popularity,poster_path  = imageViewToByte(itemView.iv_movie),original_language =  part.original_language,original_title =  part.original_title,genre_ids =  part.genre_ids,backdrop_path =  imageViewToByte(itemView.iv_movie),adult =  part.adult,overview =  part.overview,release_date =  part.release_date))

*/

            itemView.setOnClickListener { clickListener(part)}
        }
    }


}


