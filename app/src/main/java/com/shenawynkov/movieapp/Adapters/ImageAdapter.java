package com.shenawynkov.movieapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shenawynkov.movieapp.Models.Movie;
import com.shenawynkov.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shenawynkov on 4/22/2016.
 */
public class ImageAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context con;
    ArrayList<Movie> paths=new ArrayList<Movie>();
    Movie mlock=new Movie();
    public ImageAdapter(Context context)
    {

        con=context;
    }
    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Movie getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= new ViewHolder();
        inflater=(LayoutInflater)
                con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(holder);


        }  else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        String image_url = "http://image.tmdb.org/t/p/w185" +getItem(position).getPath();
        Picasso.with(con).load(image_url).into(holder.imageView);


        return convertView     ;
    }
    class  ViewHolder
    {
        ImageView imageView;

    }

    public void add(Movie path)
    {  synchronized (mlock) {
        paths.add(path);

    }

        notifyDataSetChanged();

    }
    public  void clear()
    {
        synchronized (mlock) {

            paths.clear();
        }
        notifyDataSetChanged();

    }
}
