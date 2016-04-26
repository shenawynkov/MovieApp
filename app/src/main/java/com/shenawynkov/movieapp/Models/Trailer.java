package com.shenawynkov.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shenawynkov on 4/22/2016.
 */
public class Trailer implements Parcelable {
   private String key;
   private String name;


    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;

    }
    public Trailer()
    {

    }

    public Trailer(JSONObject object) throws JSONException {
        this.key=object.getString("key");
        this.name=object.getString("name");
    }


    protected Trailer(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);

    }
}
