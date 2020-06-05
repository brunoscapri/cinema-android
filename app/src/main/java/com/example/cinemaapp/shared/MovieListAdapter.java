package com.example.cinemaapp.shared;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.controllers.MovieActivity;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.stores.MovieStore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    private List<MovieModel> movies = MovieStore.getInstance().getMovies();
    Context context;

    public MovieListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieHolder holder, final int position) {

        final MovieModel movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(movie.getYear());
        holder.movieGenre.setText(movie.getGenre());
        holder.movieRating.setText(movie.getImdbRating());
        ArrayList<String> images = movie.getImages();
        Picasso.get().load(images.get(0)).into(holder.movieImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,
                        MovieActivity.class
                );
                String pos = String.valueOf(position);
                System.out.println(pos);
                intent.putExtra("movie", movie);
                intent.putExtra("position", pos);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        TextView movieYear;
        TextView movieGenre;
        TextView movieRating;
        ImageView movieImage;

        public MovieHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieGenre = itemView.findViewById(R.id.movieGenre);
            movieRating = itemView.findViewById(R.id.movieRating);
            movieImage = itemView.findViewById(R.id.movieImage);

        }

    }
}