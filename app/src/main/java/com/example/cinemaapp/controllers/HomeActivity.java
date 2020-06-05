package com.example.cinemaapp.controllers;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.models.MovieModel;

import com.example.cinemaapp.shared.MovieListAdapter;
import com.example.cinemaapp.stores.MovieStore;
import com.example.cinemaapp.stores.UserStore;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Gson gson = new Gson();
    ArrayList<MovieModel> movies = new ArrayList<>();
    MovieListAdapter adapter = new MovieListAdapter(this);
    Button bFilter;
    RecyclerView movieList;
    TextView label;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final AppBarLayout appBar = findViewById(R.id.appbar);
        final RealtimeBlurView blurView = findViewById(R.id.blurView);
        blurView.setAlpha(1F);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
                float offsetAlpha = (appBarLayout.getY() / appBar.getTotalScrollRange());

                blurView.setAlpha( 1 - (offsetAlpha * -1));
            }
        });

        movieList = findViewById(R.id.movieList);
        movieList.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        movieList.setLayoutManager(manager);

        bFilter = findViewById(R.id.bFilter);
        label = findViewById(R.id.label);

        returnAllMovies();



        bFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    public void returnAllMovies(){
        db.collection("movies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                JsonElement jsonElement = gson.toJsonTree(data);
                                MovieModel movie = gson.fromJson(jsonElement, MovieModel.class);
                                movies.add(movie);
                            }
                            returnFavoriteMovies();
                            updateStore();
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnFavoriteMovies(){

        //getting user's favorite ids
        db.collection("users").whereEqualTo("userUID", UserStore.getInstance().getUserUID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> ids = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        System.out.println("salve meus compadres" + data);
                    }

                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    void updateStore(){
        MovieStore.getInstance().setMovies(movies);
        adapter.notifyDataSetChanged();
    }









}
