package com.example.cinemaapp.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cinemaapp.R;
import com.example.cinemaapp.stores.UserStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button bConfirmRegister;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        bConfirmRegister = findViewById(R.id.bConfirmRegister);


        bConfirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordText.getText().toString();
                String email = emailText.getText().toString();

                if(!password.equals("") && !email.equals("")){
                    createUser(email, password);
                }

            }
        });


    }

    void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> userMap = new HashMap<>();

                            userMap.put("favorites", Arrays.asList());
                            userMap.put("userUID", user.getUid());

                            db.collection("users").document(user.getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("", "Error writing document", e);
                                        }
                                    });

                            updateAfterAuth(user);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            updateAfterAuth(null);
                        }
                    }
                });
    }

    void updateAfterAuth(FirebaseUser user){
        if(user != null){
            Toast.makeText(RegisterActivity.this, "Sucesso!",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class
            );
            //intent.putExtra()
            startActivity(intent);

        }else{
            Toast.makeText(RegisterActivity.this, ":(",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
