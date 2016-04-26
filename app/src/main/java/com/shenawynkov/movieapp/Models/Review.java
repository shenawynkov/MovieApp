package com.shenawynkov.movieapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shenawynkov on 4/22/2016.
 */
public class Review {
    private String author;
    private String content;
    public Review()
    {

    }
    public Review(JSONObject object)throws JSONException
    {
        author=object.getString("author");
        content=object.getString("content");
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
