package com.project.smartcountytraveller;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddAdminActivity extends AppCompatActivity {

//    private static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^" +
//                    "(?=.*[0-9])" + //at least 1 digit
//                    "(?=.*[a-z])" + //at least 1 lowercase letter
//                    "(?=.*[A-Z])" + //at leest 1 uppercase letter
//                    "(?=.*[@#$%^&+=])" + //at least 1 special character
//                    "(?=\\S+$)" + //no white spaces
//                    ".{6,}" + // at least 6 characters
//                    "$");

    Toolbar toolbar;
    TextInputLayout textInputLayoutAdminName;
    TextInputEditText name;
    TextInputLayout textInputLayoutAdminEmail;
    TextInputEditText email;
    TextInputLayout textInputLayoutAdminPassword;
    TextInputEditText password;
    TextInputLayout textInputLayoutConfirmAdminPassword;
    TextInputEditText confirmPassword;
    View view;
    ListView listView;
    Button addAdmin;

    DatabaseReference databaseReference;
    List<Admin> users;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        toolbar = findViewById(R.id.toolbar6);
        textInputLayoutAdminName = findViewById(R.id.textInputAdminName);
        name = findViewById(R.id.adminName);
        textInputLayoutAdminEmail = findViewById(R.id.textInputAdminEmail);
        email = findViewById(R.id.adminEmail);
        textInputLayoutAdminPassword = findViewById(R.id.textInputAdminPassword);
        password = findViewById(R.id.adminPassword);
        textInputLayoutConfirmAdminPassword = findViewById(R.id.textInputConfirmAdminPassword);
        confirmPassword = findViewById(R.id.confirmadminPassword);
        view = findViewById(R.id.View);
        listView = findViewById(R.id.listView);
        addAdmin = findViewById(R.id.btnAddAdmin);


        //show title of the app inside the toolbar
        toolbar.setTitle("Add Administrator");

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmail() | !validateconfirmpassword()) {
                    return;
                }

                String adminname = name.getText().toString();
                String adminemail = email.getText().toString();
                String adminpassword = password.getText().toString();

                //save
                if (TextUtils.isEmpty((userId))){
                  String id = databaseReference.push().getKey();
                  Admin admin = new Admin(id, adminname, adminemail, adminpassword);
                  databaseReference.child(id).setValue(admin);

                    Toast.makeText(AddAdminActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                }else{
                    //update
                    databaseReference.child(userId).child("name").setValue(adminname);

                    Toast.makeText(AddAdminActivity.this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
                }

                name.setText(null);
                email.setText(null);
                password.setText(null);
                confirmPassword.setText(null);
            }
        });
    }

   @Override
    protected void onStart(){
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               users.clear();

               for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                   Admin user = postSnapshot.getValue(Admin.class);
                   users.add(user);
               }

               UserList userAdapter = new UserList(AddAdminActivity.this, users, databaseReference, textInputLayoutAdminName, name, textInputLayoutAdminEmail, email, textInputLayoutAdminPassword, password);

               listView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateconfirmpassword(){
        String Upassword = textInputLayoutAdminPassword.getEditText(). getText().toString().trim();
        String Uconfirmpassword = textInputLayoutConfirmAdminPassword.getEditText(). getText().toString().trim();

        //if password field is empty show an error it is empty
        if (Uconfirmpassword.isEmpty()){
            textInputLayoutConfirmAdminPassword.setError("Field can't be empty");
            return false;
        }
        //if password is more than 8 characters
        else if (!Upassword.equals(Uconfirmpassword)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        else {
            textInputLayoutConfirmAdminPassword.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String Uemail = textInputLayoutAdminEmail.getEditText().getText().toString().trim();

        if (Uemail.isEmpty()) {
            textInputLayoutAdminEmail.setError("Field can't be empty");
            return false;
        }
        //add another check
        else if (!Patterns.EMAIL_ADDRESS.matcher(Uemail).matches()){
            textInputLayoutAdminEmail.setError("Please enter a valid email address.");
            return false;
        }
        else {
            textInputLayoutAdminEmail.setError(null);
//                  //to remove the space for the error message
//                  textInputEmail.setErrorEnabled(false);
            return true;
        }
    }
}
