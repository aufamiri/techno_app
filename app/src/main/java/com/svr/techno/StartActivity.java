package com.svr.techno;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        checkUser(firebaseUser);

    }

    void checkUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            startActivity(new Intent(this, SellActivity.class));
//            startActivity(new Intent(this, HomeActivity.class));

        } else {


            startActivity(new Intent(this, GettingStartedActivity.class));

        }
    }
}
