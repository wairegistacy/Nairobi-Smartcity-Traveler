package com.project.smartcountytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //reference the fields of the forgot password activity
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userEmail;
    Button userPass;

    //to be able to access the FireBase methods to retrieve the user's password
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //reference the ids of the fields of the forgot password activity
        toolbar = findViewById(R.id.toolbar3);
        progressBar = findViewById(R.id.progressBar3);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btnForgetPassword);

        //set title for the toolbar in the forgot password activity
        toolbar.setTitle("Forgot password");

        firebaseAuth = FirebaseAuth.getInstance();

        //set operations of the button
       userPass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
             firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     progressBar.setVisibility(View.GONE);
                     if (task.isSuccessful()){
                         Toast.makeText(ForgotPasswordActivity.this, "Password has been sent to your email", Toast.LENGTH_LONG).show();
                     }
                     //if the task is not successful due to reasons like the email address is not registered, the following method will be executed
                     else{
                         Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                     }

                 }
             });
           }
       });
    }
}
