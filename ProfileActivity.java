package com.project.smartcountytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
//reference fields of profile activity
    TextView userEmail;
    Button userLogout;
//    Button deleteAccount;
    ProgressBar progressBar;

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
//        deleteAccount = findViewById(R.id.btnDeleteAcc);
        progressBar = findViewById(R.id.progressBar5);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //to get the logged in user's email into the text field
        userEmail.setText(firebaseUser.getEmail());

//        deleteAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
//                dialog.setTitle("Are you sure you want to delete this account?");
//                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you will not be able to access the app.");
//               //set the two buttons(Positive button= delete, Negative button=dismiss)
//               dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                   @Override
//                   public void onClick(DialogInterface dialog, int which) {
//                       progressBar.setVisibility(View.VISIBLE);
//                     //do delete user from FireBase
//                     firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                         @Override
//                         public void onComplete(@NonNull Task<Void> task) {
//                             progressBar.setVisibility(View.GONE);
//                            //check for the condition whether the user was successful deleting the account
//                             if (task.isSuccessful()){
//                                 Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_LONG).show();
//
//                                 Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                                 //add flags that clear active tasks or active activities that is currently on the stack
//                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                 startActivity(intent);
//                             }else {
//                                 Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                             }
//                         }
//                     });
//                   }
//               });
//               //this will only dismiss the dialog box
//               dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                   @Override
//                   public void onClick(DialogInterface dialog, int which) {
//                       dialog.dismiss();
//                   }
//               });
//               //to show the dialog
//               AlertDialog alertDialog = dialog.create();
//               alertDialog.show();
//            }
//
//        });

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
