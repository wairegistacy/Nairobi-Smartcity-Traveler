package com.project.smartcountytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //referencing fields of the main activity
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText useremail;
    EditText userpassword;
    Button usersignup;
    TextView login;

    //declare instance of FireBaseAuth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //reference the ids of the fields in the main activity
        toolbar = findViewById(R.id.toolbar4);
        progressBar = findViewById(R.id.progressBar4);
        useremail = findViewById(R.id.email);
        userpassword = findViewById(R.id.password);
        usersignup = findViewById(R.id.btnUserSignup);
        login = findViewById(R.id.textViewLogin);

        //show title of the app inside the toolbar
        toolbar.setTitle("Sign Up");

        //add this to establish a connection with FireBase
        firebaseAuth = FirebaseAuth.getInstance();

        usersignup.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              // show progressBar
                                              progressBar.setVisibility(View.VISIBLE);
                                              //register user with email and password to FireBase
                                              firebaseAuth.createUserWithEmailAndPassword(useremail.getText().toString(), userpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                                      progressBar.setVisibility(View.GONE);
                                                      //if user has registered successfully, an email will be sent to current user for verification
                                                      if (task.isSuccessful()) {
                                                          firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                  if (task.isSuccessful()) {
                                                                      Toast.makeText(RegisterActivity.this, "Registered successfully. Please check your email for verification", Toast.LENGTH_SHORT).show();
                                                                      //once user has successfully registered their details
                                                                      useremail.setText("");
                                                                      userpassword.setText("");
                                                                  } else {
                                                                      Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                  }
                                                              }
                                                          });

                                                      }
                                                      //if user's data was not saved successfully in FireBase, a message will be displayed from FireBase to show it was not successful
                                                      else {
                                                          Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              });
                                          }
                                      });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                });
            }

    }
