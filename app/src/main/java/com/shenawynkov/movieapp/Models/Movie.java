package com.shenawynkov.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shenawynkov on 4/22/2016.
 */
public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private  String path;




    private String original_title;

    private  String overview;
    private String release;
    private String user_rating;
    private String id;
    public   Movie(){

    }

    public   Movie(JSONObject movie) throws JSONException
    {
        this.path=movie.getString("poster_path");
        this.original_title = movie.getString("original_title");
        this.path = movie.getString("poster_path");
        this.overview = movie.getString("overview");

        this.user_rating=movie.getString("vote_average");
        this.release = movie.getString("release_date");
        this.id=movie.getString("id");


    }

    public String getUser_rating() {
        return user_rating;
    }

    protected Movie(Parcel in) {
        path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        user_rating=in.readString();
        release = in.readString();
        id= in.readString();
    }


    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }


    public String getTitle() {
        return original_title;
    }



    public String getOverview() {
        return overview;
    }

    public String getRelease() {
        return release;
    }








    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(original_title);


        dest.writeString(overview);
        dest.writeString(user_rating);
        dest.writeString(release);
        dest.writeString(id);
    }
}
