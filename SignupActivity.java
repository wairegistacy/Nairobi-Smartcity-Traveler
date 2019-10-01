package com.project.smartcountytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.regex.Pattern;



public class SignupActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])"+ //at least 1 digit
                    "(?=.*[a-z])"+ //at least 1 lowercase letter
                    "(?=.*[A-Z])"+ //at leest 1 uppercase letter
                    "(?=.*[@#$%^&+=])"+ //at least 1 special character
                    "(?=\\S+$)"+ //no white spaces
                    ".{6,}"+ // at least 6 characters
                    "$");

    //referencing fields of the main activity
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userName;
    EditText userEmail;
    EditText userPassword;
    EditText confirmPassword;
    Button userSignUp;
    TextView login;
    TextInputLayout textInputUsername;
    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;
    TextInputLayout textInputConfirmPassword;

    //declare instance of FireBaseAuth
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    List<Users> users;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //reference the ids of the fields in the main activity
        toolbar = findViewById(R.id.toolbar4);
        progressBar = findViewById(R.id.progressBar4);
        userName = findViewById(R.id.username);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmpassword);
        userSignUp = findViewById(R.id.btnUserSignup);
        login = findViewById(R.id.textViewLogin);
        textInputUsername = findViewById(R.id.textInputUsername);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        //show title of the app inside the toolbar
        toolbar.setTitle("Sign Up");

        //add this to establish a connection with FireBase
        firebaseAuth = FirebaseAuth.getInstance();

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show progressBar
                progressBar.setVisibility(View.VISIBLE);

                if (!validateEmail() | !validatePassword() | !validateconfirmpassword()) {
                    return;
                }




                //register user with email and password to FireBase
                firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        //if user has registered successfully, an email will be sent to current user for verification
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Registered successfully. Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        //once user has successfully registered their details return the fields to be empty
                                        userEmail.setText("");
                                        userPassword.setText("");
                                        confirmPassword.setText("");
                                    } else {
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        //if user's data was not saved successfully in FireBase, a message will be displayed from FireBase to show it was not successful
                        else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String name = userName.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                //save
                if (TextUtils.isEmpty((userId))){
                    String id = databaseReference.push().getKey();
                    Users user = new Users(id, name, email, password);
                    databaseReference.child(id).setValue(user);

                }
                userName.setText(null);
                userEmail.setText(null);
                userPassword.setText(null);
                confirmPassword.setText(null);
            }


        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }

//showing email validation
            private boolean validateEmail() {
                String emailInput = textInputEmail.getEditText().getText().toString().trim();

                if (emailInput.isEmpty()) {
                    textInputEmail.setError("Field can't be empty");
                    return false;
                }
                //add another check
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                    textInputEmail.setError("Please enter a valid email address.");
                    return false;
                }
                else {
                  textInputEmail.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
                  return true;
              }
            }

            //showing password validation
            private boolean validatePassword(){
                String passwordInput = textInputPassword.getEditText(). getText().toString().trim();

                //if password field is empty show an error it is empty
                if (passwordInput.isEmpty()){
                    textInputPassword.setError("Field can't be empty");
                    return false;
                }
                //if password is more than 8 characters
                else if (passwordInput.length() >8){
                    textInputPassword.setError("Password too long");
                    return false;
                }
                //to check if password is weak
                else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
                    textInputPassword.setError("Password too weak. ensure you have used at least one uppercase letter,lowercase letter, a number and a character.");
                    return false;
                }
                else {
                    textInputPassword.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
                    return true;
                }
            }
            private boolean validateconfirmpassword(){
                String passwordInput = textInputPassword.getEditText(). getText().toString().trim();
                String confirmpasswordInput = textInputConfirmPassword.getEditText(). getText().toString().trim();

                //if password field is empty show an error it is empty
                if (confirmpasswordInput.isEmpty()){
                    textInputConfirmPassword.setError("Field can't be empty");
                    return false;
                }
                //if password is more than 8 characters
                else if (!passwordInput.equals(confirmpasswordInput)){
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return false;
                }

                else {
                    textInputConfirmPassword.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
                    return true;
                }
            }
            
            
    }
            

    

