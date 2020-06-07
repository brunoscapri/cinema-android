package com.example.cinemaapp.stores;

import com.example.cinemaapp.models.MovieModel;

import java.util.ArrayList;

public class MovieStore {
    private MovieStore(){}

    private static MovieStore instance = new MovieStore();

    ArrayList<MovieModel> currentMovies = new ArrayList<>();
    ArrayList<MovieModel> favorites = new ArrayList<>();
    ArrayList<MovieModel> allMovies = new ArrayList<>();

    public static MovieStore getInstance() {
        return instance;
    }

    public void setCurrentMovies(ArrayList<MovieModel> movies) {
        this.currentMovies.clear();
        this.currentMovies.addAll(movies);

    }

    public void setAllMovies(ArrayList<MovieModel> movies) {
        this.allMovies.clear();
        this.allMovies.addAll(movies);

    }

    public void setFavorites(ArrayList<MovieModel> favorites) {
        this.favorites.clear();
        this.favorites.addAll(favorites);

    }

    public ArrayList<MovieModel> getCurrentMovies() {
        return currentMovies;
    }

    public ArrayList<MovieModel> getFavorites() {
        return favorites;
    }

    public ArrayList<MovieModel> getAllMovies() {
        return allMovies;
    }


    public void popFavorites(MovieModel movieToPop){
        for(MovieModel favorite : favorites){
            if(movieToPop.getTitle().equals(favorite.getTitle())){
                favorites.remove(favorite);
            }
        }

    }

    public void pushFavorites(MovieModel movieToPush){
        favorites.add(movieToPush);
    }

}
