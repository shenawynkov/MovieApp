package com.shenawynkov.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.shenawynkov.movieapp.Adapters.ReviewAdapter;
import com.shenawynkov.movieapp.Adapters.TrailerAdapter;
import com.shenawynkov.movieapp.Models.Movie;
import com.shenawynkov.movieapp.Models.Review;
import com.shenawynkov.movieapp.Models.Trailer;
import com.squareup.picasso.Picasso;

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


public class Detail_ActivityFragment extends Fragment {
    Movie movie;

    ExpandableHeightListView listView;

    public Detail_ActivityFragment() {
    }

    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_, container, false);


        Bundle args;
args=getArguments();
        if(args!=null) {
            movie=args.getParcelable("movie");

            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_th);
    String image_url = "http://image.tmdb.org/t/p/w185" + movie.getPath();
    Picasso.with(getActivity()).load(image_url).into(imageView);
    TextView title_tv = (TextView) rootView.findViewById(R.id.otitle);
    title_tv.setText(movie.getTitle());
    TextView overview = (TextView) rootView.findViewById(R.id.descreption);
    overview.setText(movie.getOverview());

    TextView user_rating = (TextView) rootView.findViewById(R.id.vote_average);
    user_rating.setText(movie.getUser_rating() + "/10");
    TextView release_tv = (TextView) rootView.findViewById(R.id.release);
    release_tv.setText(movie.getRelease());
    final Button button = (Button) rootView.findViewById(R.id.favorite);
    SharedPreferences sharedPref = getActivity().getSharedPreferences(
            "pref", Context.MODE_PRIVATE);
    if (sharedPref.contains(movie.getId())) {
        {
            button.setBackgroundColor(getActivity().getResources().getColor(R.color.fav));

            button.setText("Delete From Favorite");
        }
    }


    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    "pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (sharedPref.contains(movie.getId())) {
                editor.remove(movie.getId());
                editor.apply();
                button.setText("Move To Favorite");
                button.setBackgroundColor(getActivity().getResources().getColor(R.color.unfav));
            } else {
                editor.putString(movie.getId(), movie.getId());
                editor.commit();
                button.setBackgroundColor(getActivity().getResources().getColor(R.color.fav));
                button.setText("Delete From Favorite");
            }

        }
    });

    listView = (ExpandableHeightListView) rootView.findViewById(R.id.list_trailer);
    trailerAdapter = new TrailerAdapter(getActivity());
    listView.setAdapter(trailerAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Trailer t = (Trailer) listView.getAdapter().getItem(position);
            Intent intent1 = new Intent(Intent.ACTION_VIEW);


            intent1.setData(Uri.parse("http://www.youtube.com/watch?v=" + t.getKey()));
            startActivity(intent1);
        }
    });

    ExpandableHeightListView list_review = (ExpandableHeightListView) rootView.findViewById(R.id.revie_list);
    reviewAdapter = new ReviewAdapter(getActivity());
    list_review.setAdapter(reviewAdapter);

    list_review.setExpanded(true);
    listView.setExpanded(true);

}

        return rootView;


    }

    @Override
    public void onStart() {



        if(movie!=null) {



            FetchTrailersTask fetch_trailer_data = new FetchTrailersTask();
            fetch_trailer_data.execute(movie.getId());
           FetchReviewsTask fetchReviewsTask = new FetchReviewsTask();
            fetchReviewsTask.execute(movie.getId());

        }

        super.onStart();
    }


    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                }
            }

            return results;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_API)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
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
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> result) {
            super.onPostExecute(result);
            if (result != null) {
                trailerAdapter.clear();
                for (Trailer t : result) {
                    trailerAdapter.add(t);
                }
                trailerAdapter.notifyDataSetChanged();
            }
        }
    }


    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Review> results = new ArrayList<>();

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject model = trailerArray.getJSONObject(i);


                Review review = new Review(model);
                results.add(review);

            }

            return results;
        }

        @Override
        protected List<Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_API)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
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
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Review> result) {
            super.onPostExecute(result);

            if (result != null) {
                reviewAdapter.clear();
                for (Review t : result) {
                    reviewAdapter.add(t);
                }
                reviewAdapter.notifyDataSetChanged();

            }
        }
    }

}