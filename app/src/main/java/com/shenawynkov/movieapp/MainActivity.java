package com.shenawynkov.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shenawynkov.movieapp.Models.Movie;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    Boolean Tablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(findViewById(R.id.fragment_detail)!=null)
        {
            Tablet=true;
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail,new Detail_ActivityFragment());
        }
        else
            Tablet=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (Tablet)

        {

            Bundle args = new Bundle();
            args.putParcelable("movie", movie);
            Fragment fragment = new Detail_ActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detail, fragment).commit();

        } else {
            Intent intent = new Intent(this, Detail_Activity.class).putExtra("movie", movie);
            startActivity(intent);
        }
    }}
