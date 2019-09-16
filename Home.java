package com.project.smartcountytraveller;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//this class checks the current session of the FireBase user
public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //check the status of the user
        //if user has not logged out and email is verified, the user will be sent directly to the profile activity
        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            startActivity( new Intent(Home.this, ProfileActivity.class));
        }
    }
}
