package com.example.cinemaapp.controllers;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
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


import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Gson gson = new Gson();
    ArrayList<String> favoriteIds = new ArrayList<>();
    ArrayList<String> genres = new ArrayList<>();
    Spinner spinner;
    CheckBox checkBox;


    MovieListAdapter adapter = new MovieListAdapter(this);
    Button bFilter;
    RecyclerView movieList;
    TextView label;

    @Override
    protected void onRestart() {
        super.onRestart();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

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

        spinner = findViewById(R.id.spinner);

        returnAllMovies();
        returnFavoriteMovies();


        bFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = onCreateDialog();
                dialog.show();
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
                            ArrayList<MovieModel> allMovies = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                JsonElement jsonElement = gson.toJsonTree(data);
                                MovieModel movie = gson.fromJson(jsonElement, MovieModel.class);
                                movie.setDocumentID(document.getId());
                                allMovies.add(movie);
                            }
                            updateStoreAllMovies(allMovies);
                            returnGenre();
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnFavoriteMovies(){

        String userUID = UserStore.getInstance().getUserUID();
        db.collection("users")
                .whereEqualTo("userUID", userUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //existe apenas 1 documento n necessita for
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                favoriteIds = (ArrayList<String>) data.get("favorites");
                            }

                            fetchFavorites();
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void fetchFavorites(){
        MovieStore.getInstance().setFavorites(new ArrayList<MovieModel>());
        final ArrayList<MovieModel> favoriteMovies = new ArrayList<>();

        for(final String favorite : favoriteIds){
            db.collection("movies").document(favorite)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot result = (DocumentSnapshot) task.getResult();
                                Map<String, Object> data = result.getData();

                                JsonElement jsonElement = gson.toJsonTree(data);
                                MovieModel movie = gson.fromJson(jsonElement, MovieModel.class);
                                movie.setDocumentID(favorite);

                                favoriteMovies.add(movie);
                                MovieStore.getInstance().setFavorites(favoriteMovies);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    void updateToFavorites(ArrayList<MovieModel> favoriteMovies){
        MovieStore.getInstance().setCurrentMovies(favoriteMovies);
        adapter.notifyDataSetChanged();
        label.setText("Favoritos");
    }



    void updateStoreAllMovies(ArrayList<MovieModel> allMovies){
        MovieStore.getInstance().setAllMovies(allMovies);
        MovieStore.getInstance().setCurrentMovies(allMovies);
        adapter.notifyDataSetChanged();
    }


    public Dialog onCreateDialog() {

        View inflatedView = getLayoutInflater().inflate(R.layout.filter_modal, null);
        spinner = inflatedView.findViewById(R.id.spinner);
        checkBox = inflatedView.findViewById(R.id.checkBox);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_item, genres);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(inflatedView)
                .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String selection = (String) spinner.getSelectedItem();
                        boolean check = checkBox.isChecked();
                        handleFilter(selection, check);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }

    void returnGenre(){
        genres.clear();
        genres.add("Todos");
        for(MovieModel movie: MovieStore.getInstance().getAllMovies()){
            String raw = movie.getGenre();

            String replaced = raw.replace(" ", "");
            String[] split = replaced.split(",");

            for(String gr: split){
                if(!genres.contains(gr)){
                    genres.add(gr);
                }
            }

        }
    }

    void handleFilter(String selection, boolean check){
        ArrayList<MovieModel> filteredMovies = new ArrayList<>();
        label.setText(selection);

        //caso for apenas nos favoritos
        if(check){
            //caso a opcao seja todos
            if(selection.equals("Todos")){
                updateToFavorites(MovieStore.getInstance().getFavorites());
            }else{
                for(MovieModel movie : MovieStore.getInstance().getFavorites()){

                    String raw = movie.getGenre();
                    String replaced = raw.replace(" ", "");
                    String[] split = replaced.split(",");

                    for(String gr:split){
                        if(gr.equals(selection)){
                            filteredMovies.add(movie);
                        }
                    }
                }
                MovieStore.getInstance().setCurrentMovies(filteredMovies);
                adapter.notifyDataSetChanged();
            }
        }
        else{
            if(selection.equals("Todos")){
                returnAllMovies();
            }else{
                for(MovieModel movie: MovieStore.getInstance().getAllMovies()){
                    String raw = movie.getGenre();

                    String replaced = raw.replace(" ", "");
                    String[] split = replaced.split(",");

                    for(String gr:split){
                        if(gr.equals(selection)){
                            filteredMovies.add(movie);
                        }
                    }
                }
                MovieStore.getInstance().setCurrentMovies(filteredMovies);
                adapter.notifyDataSetChanged();
            }

        }

    }
}
