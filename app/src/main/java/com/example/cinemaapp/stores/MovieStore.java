package com.example.cinemaapp.stores;

import com.example.cinemaapp.models.MovieModel;

import java.util.ArrayList;

public class MovieStore {
    //private MovieStore(){}

    private static MovieStore instance = new MovieStore();

    ArrayList<MovieModel> movies = new ArrayList<>();
    ArrayList<MovieModel> favorites = new ArrayList<>();

    public static MovieStore getInstance() {
        return instance;
    }

    public void setMovies(ArrayList<MovieModel> movies) {
        this.movies.clear();
        this.movies.addAll(movies);

    }

    public void setFavorites(ArrayList<MovieModel> favorites) {
        this.favorites.clear();
        this.favorites.addAll(favorites);

    }

    public ArrayList<MovieModel> getMovies() {
        return movies;
    }

    public ArrayList<MovieModel> getFavorites() {
        return favorites;
    }

    public void popFavorites(MovieModel movieToPop){
        favorites.remove(movieToPop);

    }

    public void pushFavorites(MovieModel movieToPush){
        favorites.add(movieToPush);
    }

}
