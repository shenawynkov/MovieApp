package com.shenawynkov.movieapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenawynkov.movieapp.Models.Review;
import com.shenawynkov.movieapp.R;

import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends BaseAdapter {

    List<Review> reviews =new ArrayList<Review>();
    Context con;
    private final Review mLock = new Review();


    public ReviewAdapter(Context context  )
    {
        this.con=context;
    }
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position);
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
        { convertView=inflater.inflate(R.layout.review_item, parent, false);
            viewholder=new Viewholder(convertView);


            convertView.setTag(viewholder);

        }
        else
        {
            viewholder=(Viewholder) convertView.getTag();
        }
        viewholder.author.setText(getItem(position).getAuthor());
        viewholder.content.setText(getItem(position).getContent());



        return  convertView;
    }
    public static  class Viewholder{

        TextView author;
        TextView content;
        public Viewholder(View view) {
            author=(TextView)view.findViewById(R.id.review_author);
            content=(TextView)view.findViewById(R.id.review_content);

        }

    }
    public  void add(Review ob)

    {

        synchronized (mLock) {
            reviews.add(ob);
        }
        notifyDataSetChanged();

    }
    public  void clear( )

    {

        synchronized (mLock) {
            reviews.clear();
        }
        notifyDataSetChanged();

    }

}





































/*

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<Review> reviews=new ArrayList<Review>();
    private final Review mLock = new Review();
    Context con;

public  ReviewAdapter(Context con, List<Review> reviews)
{
    this.con=con;
    this.reviews=reviews;
}
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        public ViewHolder(View view)
        {
            super(view);

            author=(TextView)view.findViewById(R.id.review_author);
            content=(TextView)view.findViewById(R.id.review_content);
        }
    }
    public  void add(Review ob)

    {

        synchronized (mLock) {
            reviews.add(ob);
        }
        notifyDataSetChanged();

    }
    public  void clear( )

    {

        synchronized (mLock) {
            reviews.clear();
        }
        notifyDataSetChanged();

    }


}*/


