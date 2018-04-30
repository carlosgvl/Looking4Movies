package com.example.carlosvivas.looking4movies.View


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.carlosvivas.looking4movies.Controller.MovieAdapter
import com.example.carlosvivas.looking4movies.Model.Movie

import com.example.carlosvivas.looking4movies.R
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.API_KEY
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.POPULAR
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.SERIE
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.TOP_RATED
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.UPCOMING
import com.example.carlosvivas.looking4movies.Utils.Connectionconstans.pageNumber
import kotlinx.android.synthetic.main.fragment_series.view.*
import org.json.JSONException
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class SeriesFragment : Fragment() {
    private var rv_series: RecyclerView?= null
    private var queue_series: RequestQueue? = null
    private var url: String? = null
    private var btn_popular: Button? = null
    private var btn_top_rated: Button? = null
    private var btn_upcoming: Button? = null
    private var progress: ProgressBar?= null
    private  var series_array = ArrayList<Movie>()
    var adapter: MovieAdapter?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val series_view = inflater.inflate(R.layout.fragment_series, container, false)

        setHasOptionsMenu(true)

        rv_series= series_view.findViewById<RecyclerView>(R.id.rv_series)
        btn_popular = series_view.findViewById<Button>(R.id.btn_popular)
        btn_top_rated = series_view.findViewById<Button>(R.id.btn_top_rated)
        btn_upcoming = series_view.findViewById<Button>(R.id.btn_upcoming)
        progress = series_view.findViewById<ProgressBar>(R.id.progressBar)


        rv_series!!.layoutManager = GridLayoutManager(activity, 3)

        rv_series!!.hasFixedSize()
        queue_series = Volley.newRequestQueue(activity)
        queue_series!!.cache.clear()

        Visibility(R.drawable.button_pressed, R.drawable.button_white, R.drawable.button_white)
        url = Connectionconstans.URL_APPLICATION + SERIE+ POPULAR + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
        ConsultarSeries()

        btn_popular!!.setOnClickListener {


            Visibility(R.drawable.button_pressed, R.drawable.button_white, R.drawable.button_white)
            url = Connectionconstans.URL_APPLICATION + SERIE + POPULAR + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarSeries()
        }

        btn_top_rated!!.setOnClickListener {


            Visibility( R.drawable.button_white,R.drawable.button_pressed, R.drawable.button_white)
            url = Connectionconstans.URL_APPLICATION + SERIE + TOP_RATED + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarSeries()
        }

        btn_upcoming!!.setOnClickListener {
            Visibility( R.drawable.button_white, R.drawable.button_white,R.drawable.button_pressed)
           /* url = Connectionconstans.URL_APPLICATION + SERIE + UPCOMING + "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;
            ConsultarSeries()*/


        }


        return series_view
    }

    fun Visibility(popular: Int, top_rate: Int, up_coming: Int){
        btn_popular!!.setBackgroundResource(popular)
        btn_top_rated!!.setBackgroundResource(top_rate)
        btn_upcoming!!.setBackgroundResource(up_coming)

    }

    fun ConsultarSeries() {

        progress!!.visibility=View.VISIBLE


        val myRequest = JsonObjectRequest(Request.Method.GET, url,null,

                Response.Listener { response ->

                    Log.d("Serie response ",response.toString())
                    try {
                        var movies_category=response.optJSONArray("results")

                        series_array = ArrayList<Movie>()
                        series_array.clear()


                        Log.d("Serie Modo de ",movies_category.toString())
                        for (i in 0..(movies_category.length() - 1)) {
                            val item = movies_category.getJSONObject(i)
                            Log.d("Perfil item:  ",i.toString() +" :"+item.toString())
                            series_array.add(Movie(  item.getString("vote_count"), item.getString("id"),"false",item.getString("vote_average"),item.getString("name"),item.getString("popularity"),item.getString("poster_path"),item.getString("original_language"),item.getString("original_name"),item.getString("genre_ids"),item.getString("backdrop_path"),item.getString("origin_country"),item.getString("overview"),item.getString("first_air_date")))

                        }
                        progress!!.visibility=View.GONE
                        adapter=MovieAdapter(series_array,{ movieItem : Movie -> serieItemClicked(movieItem) })
                        rv_series!!.adapter = adapter
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.d("Premios error ", error.toString()) })



        queue_series!!.add(myRequest)

    }

    private fun serieItemClicked(movieItem : Movie) {

        val showDetailActivityIntent = Intent(activity, DetalleSerieActivity::class.java)
        showDetailActivityIntent.putExtra("title", movieItem.title)
        showDetailActivityIntent.putExtra("backdrop_path", movieItem.backdrop_path)
        showDetailActivityIntent.putExtra("release_date", movieItem.release_date)
        showDetailActivityIntent.putExtra("vote_average", movieItem.vote_average)
        showDetailActivityIntent.putExtra("vote_count", movieItem.vote_count)
        showDetailActivityIntent.putExtra("overview", movieItem.overview)
        startActivity(showDetailActivityIntent)
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

                val filteredModelList = filter(  series_array, newText)
                adapter!!.setSearchResult(filteredModelList)
                rv_series!!.adapter = adapter
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
