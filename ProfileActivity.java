package com.project.smartcountytraveller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
//reference fields of profile activity
    TextView userEmail;
    Button userLogout;

    //call the firebaseAuth
    FirebaseAuth firebaseAuth;

    //use FireBaseUser to get the details of the users who have logged in
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //reference the ids of the fields in the profile activity
        userEmail = findViewById(R.id.tvUserEmail);
        userLogout = findViewById(R.id.btnLogout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //to get the logged in user's email into the text field
        userEmail.setText(firebaseUser.getEmail());

        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to logout the user from the app by cleaning user sessions
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                //add flags that clear active tasks or active activities that is currently on the stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }
}
