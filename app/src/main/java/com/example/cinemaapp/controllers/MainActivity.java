package com.example.cinemaapp.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cinemaapp.R;
import com.example.cinemaapp.stores.UserStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ImageView popcornImage;
    Button bSignIn;
    Button bRegister;
    EditText tEmail;
    EditText tPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popcornImage = findViewById(R.id.popcornImage);
        bSignIn = findViewById(R.id.bSignIn);
        bRegister = findViewById(R.id.bRegister);
        tEmail = findViewById(R.id.tEmail);
        tPassword = findViewById(R.id.tPassword);
        popcornImage.setRotation(20);

        mAuth = FirebaseAuth.getInstance();

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = tEmail.getText().toString();
                String password = tPassword.getText().toString();

                if(!email.equals("") && !password.equals("")){
                    signIn(email, password);
                }
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        RegisterActivity.class
                );
                //intent.putExtra()
                startActivity(intent);
            }
        });


    }

    void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                            updateAfterAuth(user);
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateAfterAuth(null);
                        }
                    }
                });
    }

    void updateAfterAuth(FirebaseUser user){
        if(user != null){

            UserStore.getInstance().setEmail(user.getEmail());
            UserStore.getInstance().setUserUID(user.getUid());

            Toast.makeText(MainActivity.this, "Sucesso!",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,
                    HomeActivity.class
            );
            //intent.putExtra()
            startActivity(intent);

        }else{
            Toast.makeText(MainActivity.this, ":(",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
