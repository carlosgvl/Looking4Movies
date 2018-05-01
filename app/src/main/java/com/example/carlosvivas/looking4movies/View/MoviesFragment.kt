package com.example.carlosvivas.looking4movies.View


import android.content.Intent
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
import android.widget.ProgressBar
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.TOP_RATED
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.UPCOMING


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
    var adapter: MovieAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val movies_view = inflater.inflate(R.layout.fragment_movies, container, false)


        setHasOptionsMenu(true)

        rv_movies= movies_view.findViewById<RecyclerView>(R.id.rv_series)
        btn_popular = movies_view.findViewById<Button>(R.id.btn_popular)
        btn_top_rated = movies_view.findViewById<Button>(R.id.btn_top_rated)
        btn_upcoming = movies_view.findViewById<Button>(R.id.btn_upcoming)
        progress = movies_view.findViewById<ProgressBar>(R.id.progressBar)


        rv_movies!!.layoutManager = GridLayoutManager(activity, 3)

        rv_movies!!.hasFixedSize()
        queue = Volley.newRequestQueue(activity)
        queue!!.cache.clear()

        Visibility(R.drawable.button_pressed, R.drawable.button_white, R.drawable.button_white)
        url = URL_APPLICATION + MOVIE + POPULAR + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
        ConsultarMovies()

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

                            }
                        progress!!.visibility=View.GONE
                        adapter=MovieAdapter(movies_array,{ movieItem : Movie -> movieItemClicked(movieItem) })
                        rv_movies!!.adapter = adapter
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.d("Movies error ", error.toString()) })



        queue!!.add(myRequest)

    }

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
