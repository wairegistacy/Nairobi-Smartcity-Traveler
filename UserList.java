package com.project.smartcountytraveller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;


public class UserList extends ArrayAdapter<Admin> {

    private Activity context;
    private List<Admin> users;
    DatabaseReference databaseReference;
    TextInputLayout textInputLayoutAdminName;
    TextInputEditText name;
    TextInputLayout textInputLayoutAdminEmail;
    TextInputEditText email;
    TextInputLayout textInputLayoutAdminPassword;
    TextInputEditText password;

    public UserList(@NotNull Activity context, List<Admin> users, DatabaseReference databaseReference, TextInputLayout textInputLayoutAdminName,
                    TextInputEditText name, TextInputLayout textInputLayoutAdminEmail, TextInputEditText email, TextInputLayout textInputLayoutAdminPassword,
                    TextInputEditText password){
        super(context, R.layout.layout_user_list, users);

        this.context = context;
        this.users = users;
        this.databaseReference = databaseReference;
        this.textInputLayoutAdminName = textInputLayoutAdminName;
        this.name = name;
        this.textInputLayoutAdminEmail = textInputLayoutAdminEmail;
        this.email = email;
        this.textInputLayoutAdminPassword = textInputLayoutAdminPassword;
        this.password = password;
    }

    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView txtName = listViewItem.findViewById(R.id.txtName);
        Button delete = listViewItem.findViewById(R.id.btnDelete);
        Button update = listViewItem.findViewById(R.id.btnUpdate);

        final Admin user = users.get(pos);
        txtName.setText(user.getName());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(user.getId()).removeValue();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(user.getName());
                email.setText(user.getEmail());
                password.setText(user.getPassword());
                AddAdminActivity.userId = user.getId();
            }
        });
        return listViewItem;
    }
}
