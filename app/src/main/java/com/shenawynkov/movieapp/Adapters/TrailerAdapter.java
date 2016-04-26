package com.shenawynkov.movieapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenawynkov.movieapp.Models.Trailer;
import com.shenawynkov.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenawynkov on 4/22/2016.
 */
public class TrailerAdapter extends BaseAdapter {

    List<Trailer> trailers=new ArrayList<Trailer>();
Context con;
    private final Trailer mLock = new Trailer();


    public  TrailerAdapter(Context context  )
    {
        this.con=context;
    }
    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder;
        LayoutInflater inflater=(LayoutInflater)con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        { convertView=inflater.inflate(R.layout.trailer_item, parent, false);
            viewholder=new Viewholder(convertView);


            convertView.setTag(viewholder);

        }
        else
        {
          viewholder=(Viewholder) convertView.getTag();
        }

        String image_url = "http://img.youtube.com/vi/" + getItem(position).getKey() + "/default.jpg";
        Picasso.with(con).load(image_url)
                .into(viewholder.imageView);
       viewholder.name.setText(getItem(position).getName());
  return  convertView;
    }
     public static  class Viewholder{

        TextView name;
       ImageView imageView;
         public Viewholder(View view) {
             imageView = (ImageView) view.findViewById(R.id.trailer_img);
             name = (TextView) view.findViewById(R.id.trailer_txt);
         }

    }
    public  void add(Trailer ob)

    {

                synchronized (mLock) {
                    trailers.add(ob);
                }
        notifyDataSetChanged();

    }
    public  void clear( )

    {

        synchronized (mLock) {
          trailers.clear();
        }
        notifyDataSetChanged();

    }

}
