package com.example.carlosvivas.looking4movies.View


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.carlosvivas.looking4movies.Controller.MovieAdapter
import com.example.carlosvivas.looking4movies.Model.Movie
import com.example.carlosvivas.looking4movies.R
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.API_KEY
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.MOVIE
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.POPULAR
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.URL_APPLICATION
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.pageNumber
import org.json.JSONException
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.carlosvivas.looking4movies.Controller.MoviesDBHelper
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.TOP_RATED
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.UPCOMING
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {

    private var rv_movies: RecyclerView ?= null
    private var queue: RequestQueue? = null
    private var url: String? = null
    private var btn_popular: Button ? = null
    private var btn_top_rated: Button ? = null
    private var btn_upcoming: Button ? = null
    private var progress: ProgressBar ?= null
    private  var movies_array = ArrayList<Movie>()
    private var iv_movie: ImageView?=null
    var adapter: MovieAdapter?=null
    lateinit var usersDBHelper : MoviesDBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val movies_view = inflater.inflate(R.layout.fragment_movies, container, false)

        usersDBHelper = MoviesDBHelper(activity!!)


        setHasOptionsMenu(true)

        rv_movies= movies_view.findViewById<RecyclerView>(R.id.rv_series)
        btn_popular = movies_view.findViewById<Button>(R.id.btn_popular)
        btn_top_rated = movies_view.findViewById<Button>(R.id.btn_top_rated)
        btn_upcoming = movies_view.findViewById<Button>(R.id.btn_upcoming)
        progress = movies_view.findViewById<ProgressBar>(R.id.progressBar)
        iv_movie = movies_view.findViewById<ImageView>(R.id.iv_movie)


        rv_movies!!.layoutManager = GridLayoutManager(activity, 3)

        rv_movies!!.hasFixedSize()
        queue = Volley.newRequestQueue(activity)
        queue!!.cache.clear()

        Visibility(R.drawable.button_pressed, R.drawable.button_white, R.drawable.button_white)
        url = URL_APPLICATION + MOVIE + POPULAR + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
        ConsultarMovies()

        Log.d("MOVIE_ARRAY 1", movies_array.toString())

        btn_popular!!.setOnClickListener {

            Visibility(R.drawable.button_pressed, R.drawable.button_white, R.drawable.button_white)
            url = URL_APPLICATION + MOVIE + POPULAR + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarMovies()
        }

        btn_top_rated!!.setOnClickListener {
            Visibility( R.drawable.button_white,R.drawable.button_pressed, R.drawable.button_white)
            url = URL_APPLICATION + MOVIE + TOP_RATED + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarMovies()
        }

        btn_upcoming!!.setOnClickListener {
            Visibility( R.drawable.button_white, R.drawable.button_white,R.drawable.button_pressed)
            url = URL_APPLICATION + MOVIE + UPCOMING + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarMovies()
        }


        
        return movies_view
    }



    private fun movieItemClicked(movieItem : Movie) {

        val showDetailActivityIntent = Intent(activity, DetalleActivity::class.java)
        showDetailActivityIntent.putExtra("title", movieItem.title)
        showDetailActivityIntent.putExtra("poster_path", movieItem.poster_path)
        showDetailActivityIntent.putExtra("release_date", movieItem.release_date)
        showDetailActivityIntent.putExtra("vote_average", movieItem.vote_average)
        showDetailActivityIntent.putExtra("vote_count", movieItem.vote_count)
        showDetailActivityIntent.putExtra("overview", movieItem.overview)
        startActivity(showDetailActivityIntent)
    }

    fun Visibility(popular: Int, top_rate: Int, up_coming: Int){
        btn_popular!!.setBackgroundResource(popular)
        btn_top_rated!!.setBackgroundResource(top_rate)
        btn_upcoming!!.setBackgroundResource(up_coming)

    }


    fun ConsultarMovies() {

        progress!!.visibility=View.VISIBLE

        val myRequest = JsonObjectRequest(Request.Method.GET, url,null,

                Response.Listener { response ->
                    try {
                        var movies_category=response.optJSONArray("results")



                            movies_array.clear()


                            Log.d("Perfil Modo de ",movies_category.toString())
                            for (i in 0..(movies_category.length() - 1)) {
                                val item = movies_category.getJSONObject(i)
                                Log.d("Perfil item:  ",i.toString() +" :"+item.toString())
                                movies_array.add(Movie(  item.getString("vote_count"), item.getString("id"),item.getString("video"),item.getString("vote_average"),item.getString("title"),item.getString("popularity"),item.getString("poster_path"),item.getString("original_language"),item.getString("original_title"),item.getString("genre_ids"),item.getString("backdrop_path"),item.getString("adult"),item.getString("overview"),item.getString("release_date")))

                               //var result = usersDBHelper!!.insertUser(MovieSq(  vote_count = item.getString("vote_count"), id = item.getString("id"),video =  item.getString("video"),vote_average =  item.getString("vote_average"),title =  item.getString("title"),popularity =  item.getString("popularity"),poster_path  = imageViewToByte(iv_movie!!),original_language =  item.getString("original_language"),original_title =  item.getString("original_title"),genre_ids =  item.getString("genre_ids"),backdrop_path =  imageViewToByte(iv_movie!!),adult =  item.getString("adult"),overview =  item.getString("overview"),release_date =  item.getString("release_date")))
                            }
                        adapter=MovieAdapter(movies_array,{ movieItem : Movie -> movieItemClicked(movieItem) })
                        rv_movies!!.adapter = adapter
                        progress!!.visibility=View.GONE

                        Log.d("MOVIE_ARRAY 2", movies_array.toString())

                        /*  adapter=MovieAdapter(movies_array,{ movieItem : Movie -> movieItemClicked(movieItem) })
                          rv_movies!!.adapter = adapter*/
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.d("Movies error ", error.toString()) })



        queue!!.add(myRequest)

    }


    fun imageViewToByte(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

/*    fun addUser(v:View){

        var result = usersDBHelper!!.insertUser(UserModel(userid = userid,name = name, image=imageViewToByte(imageView!!)))



    }

    fun deleteUser(v:View){
        var userid = this.edittext_userid.text.toString()
        val result = usersDBHelper!!.deleteUser(userid)
        this.textview_result.text = "Deleted user : "+result
        this.ll_entries.removeAllViews()
    }

    fun showAllUsers(v: View){


        var users = usersDBHelper!!.readAllMovies()
        users.forEach {
            var tv_user = TextView(this)
            var tv_image = ImageView(this)
            tv_user.textSize = 30F
            tv_user.text = it.userid+". "+it.name.toString()
            Log.d("cursor", it.userid+". "+it.name.toString())


            val foodImage = it.image
            val bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.size)
            tv_image.setImageBitmap(bitmap)
            this.ll_entries.addView(tv_user)
            this.ll_entries.addView(tv_image)
        }

        var user = usersDBHelper!!.readMovie("12")
        user.forEach {
            this.textview_result.text =  " user:  "+it.userid+" "+it.name
        }

    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        var searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("Buscar",newText);

                val filteredModelList = filter(  movies_array, newText)
                adapter!!.setSearchResult(filteredModelList)
                rv_movies!!.adapter = adapter
                return true
            }

        })


    }

    private fun filter(models: List<Movie>, query: String): List<Movie> {
        var query = query
        query = query.toLowerCase()
        val filteredModelList =  ArrayList<Movie>()
        for (model in models) {
            val text = model.title.toLowerCase()
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

}// Required empty public constructor
