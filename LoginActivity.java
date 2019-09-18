package com.project.smartcountytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userEmail;
    EditText userPassword;
    Button userLogin;
    TextView forgotpassword;
    TextInputLayout textInputemail;
    TextInputLayout textInputpassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar2);
        progressBar = findViewById(R.id.progressBar2);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userLogin = findViewById(R.id.btnUserLogin);
        forgotpassword = findViewById(R.id.textViewCreateAccount);
        textInputemail = findViewById(R.id.textInputEmail2);
        textInputpassword = findViewById(R.id.textInputPassword2);


        toolbar.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (!validateloginEmail() | !validateloginPassword()){
                    return;
                }

                //log in and check in firebase if email and password details exist
                firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        //Here is where we check for the results based on the task
                        if (task.isSuccessful()) {
                            //check if user has verified email address
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                //if login is successful, one will be directed to profile activity page
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //if login is not successful, then the toast message from the Firebase sends to our app will be shown
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    //showing email validation
    private boolean validateloginEmail() {
        String emailInput = textInputemail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputemail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputemail.setError("Please enter a valid email address.");
            return false;

        }else {
            textInputemail.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    //showing password validation
    private boolean validateloginPassword() {
        String passwordInput = textInputpassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputpassword.setError("Field can't be empty");
            return false;
        }
//        //if password is more than 8 characters
//        else if (passwordInput.length() >8){
//            textInputpassword.setError("Password too long");
//            return false;
//        }
        else {
            textInputpassword.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
            return true;
        }
    }
}