package com.example.cinemaapp.stores;

import com.example.cinemaapp.models.MovieModel;

import java.util.ArrayList;

public class MovieStore {
    //private MovieStore(){}

    private static MovieStore instance = new MovieStore();

    ArrayList<MovieModel> movies = new ArrayList<>();


    public static MovieStore getInstance() {
        return instance;
    }

    public void setMovies(ArrayList<MovieModel> movies) {
        this.movies.clear();
        this.movies.addAll(movies);

    }

    public ArrayList<MovieModel> getMovies() {
        return movies;
    }
}
