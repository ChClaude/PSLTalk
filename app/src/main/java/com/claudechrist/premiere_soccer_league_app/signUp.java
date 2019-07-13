package com.claudechrist.premiere_soccer_league_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUp extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //name = findViewById(R.id.nameDetails);
        email = findViewById(R.id.emailDetails1);
        password = (EditText)findViewById(R.id.passwordDetails);
        signUpButton = findViewById(R.id.signUpButton);


        //signUpButton.setOnClickListener(new View.OnClickListener() {





        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
            }

    public void onRegister(View view){

       final String myEmail = email.getText().toString();
       final String myPassword = password.getText().toString();

       if(TextUtils.isEmpty(myEmail) || TextUtils.isEmpty(myPassword)) {
           Toast.makeText(signUp.this, "Details missing", Toast.LENGTH_SHORT).show();
       }else {
           mAuth.createUserWithEmailAndPassword(myEmail, myPassword)
                   .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               // Sign in success, update UI with the signed-in user's information
                               Log.i("TAG", "createUserWithEmail:success");
                               Toast.makeText(signUp.this, "Successful", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(signUp.this, LoginActivity.class);
                               startActivity(intent);
                           } else {
                               // If sign in fails, display a message to the user.
                               Log.i("TAG", "createUserWithEmail:failure", task.getException());
                               Toast.makeText(signUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                           }

                           // ...
                       }
                   });
       }

    }
}
