package com.shenawynkov.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shenawynkov.movieapp.Adapters.ImageAdapter;
import com.shenawynkov.movieapp.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivityFragment extends Fragment {
    public interface Callback {
        void onItemSelected(Movie movie);

    }


    public ImageAdapter imageAdapter;
    public MainActivityFragment() {
    }
    private  String sort_by="popular";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainfragment, menu);
        MenuItem top_rated = menu.findItem(R.id.top_rated);
        MenuItem most_pop = menu.findItem(R.id.popular);
        MenuItem fav = menu.findItem(R.id.favorite);
        if (sort_by.contentEquals("popular")) {
            if (!most_pop.isChecked())
                most_pop.setChecked(true);
        } else if (sort_by.contentEquals("top_rated"))
        {if (!top_rated.isChecked())
                top_rated.setChecked(true);
    }
            else if(sort_by.contentEquals("favorite"))
                if(!fav.isChecked())
                    fav.setChecked(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.top_rated:
                sort_by="top_rated";
                if (item.isChecked())
                    item.setChecked(false);
                else
                { item.setChecked(true);
                    updatemovie(sort_by);
                }


                return true;

            case R.id.popular:
                sort_by="popular";
                if(item.isChecked())
                    item.setChecked(false);
                else {
                    item.setChecked(true);

                    updatemovie(sort_by);
                }

            case R.id.favorite:
                    sort_by="favorite";
                    if(item.isChecked())
                        item.setChecked(false);
                    else {
                        item.setChecked(true);

                        updatemovie(sort_by);
                    }


                    return true;
            default:
                return super.onOptionsItemSelected(item);


        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootview.findViewById(R.id.grid_view);

        imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = imageAdapter.getItem(position);
                ((Callback)getActivity()).onItemSelected(movie);
            }
        });
        return rootview;
    }

    @Override
    public void onStart() {

        updatemovie(sort_by);
        super.onStart();
    }
    public void  updatemovie(String s)
    {
        if(sort_by!="favorite") {
            Fetch_movie_data fetch_movie_data = new Fetch_movie_data();
            fetch_movie_data.execute(s);
        }
        else
        {
            SharedPreferences sharedPref = getActivity().getSharedPreferences( "pref", Context.MODE_PRIVATE);
            Map<String,?> keys = sharedPref.getAll();
   imageAdapter.clear();
            for(Map.Entry<String,?> entry : keys.entrySet()){

                Fetch_movie_data_one fetch_movie_data_one=new Fetch_movie_data_one();
                fetch_movie_data_one.execute(entry.getKey());

            }
        }

    }

    public class Fetch_movie_data extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = Fetch_movie_data.class.getSimpleName();



        private List<Movie> getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";



            JSONObject movieJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);


            List<Movie> results = new ArrayList<>();


            for (int i = 0; i < movieArray.length(); i++) {




                JSONObject movieinfo = movieArray.getJSONObject(i);
                Movie movie = new Movie(movieinfo);

                results.add(movie);
            }



            return  results;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {




            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String moviestr = null;

            try {

                String baseUrl = "http://api.themoviedb.org/3/movie/";
                String apiKey = "api_key";
                String sort=params[0];

                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(sort)
                        .appendQueryParameter(apiKey,
                                BuildConfig.MOVIE_API)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                moviestr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {

                return getMovieDataFromJson(moviestr) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            if (result != null) {
                imageAdapter.clear();
                for (Movie dayForecastStr : result) {
                    imageAdapter.add(dayForecastStr);
                }
                imageAdapter.notifyDataSetChanged();

            }
        }
    }
    public class Fetch_movie_data_one extends AsyncTask<String, Void, Movie> {
        private final String LOG_TAG = Fetch_movie_data_one.class.getSimpleName();



        private Movie getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {





            JSONObject movieJson = new JSONObject(forecastJsonStr);









                return  new Movie(movieJson);
            }






        @Override
        protected Movie doInBackground(String... params) {



            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviestr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                String baseUrl = "http://api.themoviedb.org/3/movie/"+params[0];
                String apiKey = "api_key";


                Uri builtUri = Uri.parse(baseUrl).buildUpon()

                        .appendQueryParameter(apiKey,
                                BuildConfig.MOVIE_API)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviestr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {

                return getMovieDataFromJson(moviestr) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            if (result != null) {


                    imageAdapter.add(result);
                }
                imageAdapter.notifyDataSetChanged();
                //
            }
        }
    }

