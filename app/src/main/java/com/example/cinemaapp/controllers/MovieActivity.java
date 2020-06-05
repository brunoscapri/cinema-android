package com.example.cinemaapp.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.models.MovieModel;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    CarouselView carouselView;
    ArrayList<String> images;
    MovieModel movie;
    int position;

    TextView plot;
    TextView genre;
    TextView runtime;
    TextView country;
    TextView released;
    TextView metascore;
    TextView director;
    TextView actors;
    TextView writer;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (getIntent().hasExtra("movie")) {
            movie = getIntent().getExtras().getParcelable("movie");
            String pos = getIntent().getExtras().getString("position");
            position = Integer.parseInt(pos);
            images = movie.getImages();
        }else{
            images = new ArrayList<>();
        }


        plot = findViewById(R.id.plot);
        genre = findViewById(R.id.genre);
        runtime = findViewById(R.id.runtime);
        country = findViewById(R.id.country);
        released = findViewById(R.id.released);
        metascore = findViewById(R.id.metascore);
        director = findViewById(R.id.director);
        actors = findViewById(R.id.actors);
        writer = findViewById(R.id.writer);
        title = findViewById(R.id.title);

        plot.setText(movie.getPlot());
        genre.setText(movie.getGenre());
        runtime.setText(movie.getRuntime());
        country.setText(movie.getCountry());
        released.setText(movie.getReleased());
        metascore.setText(movie.getMetascore());
        director.setText(movie.getDirector());
        actors.setText(movie.getActors());
        writer.setText(movie.getWriter());
        title.setText(movie.getTitle());


        carouselView = findViewById(R.id.carouselView);

        if(images != null){
            carouselView.setPageCount(images.size());
            carouselView.setImageListener(imageListener);
        }


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(images.get(position)).into(imageView);
        }
    };
}
