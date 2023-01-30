package com.example.projecty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sign_up extends AppCompatActivity {

TextView login;
EditText email;
EditText password;
EditText username;
Button signup;
ImageView google;
ImageView facebook;
FirebaseAuth mAuth;
FirebaseFirestore data_base;
GoogleSignInClient gsc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        login = findViewById(R.id.login);

        email = findViewById(R.id.email);
        username = findViewById(R.id.editText);
        password = findViewById(R.id.Password);

        signup = findViewById(R.id.appCompatButton);
        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);

        mAuth = FirebaseAuth.getInstance();
        data_base = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = username.getText().toString();
                String email_1 = email.getText().toString();
                String password_1 = password.getText().toString();
                if(user_name.length() == 0){
                    Toast.makeText(getApplicationContext(), "User name cannot be empty", Toast.LENGTH_SHORT).show();
                }else if(email_1.length() <= 5){
                    Toast.makeText(getApplicationContext(), "email is invalid", Toast.LENGTH_LONG).show();
                }else if(password_1.length() == 0){
                    Toast.makeText(getApplicationContext(),"Password is required", Toast.LENGTH_SHORT).show();
                }else{
                     mAuth.createUserWithEmailAndPassword(email_1, password_1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {

                         if(task.isSuccessful()){

                             Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                             String message = "User has been registered: A confirmation link has been send to your email after clicking on that link you can sign in again";

                                             Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                                             startActivity(new Intent(getApplicationContext(), Login_Page.class));
                                         Map<String, Object> data = new HashMap<>();
                                         data.put("username", user_name);
                                         data_base.collection("users").document(Objects.requireNonNull(mAuth.getUid())).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG).show();
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 String message = e.getMessage();
                                                 Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                                             }
                                         });
                                             finish();
                                     }
                                     else{
                                         Toast.makeText(getApplicationContext(), "Some Error occurred", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });
                         }
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             String message = e.getMessage();
                             Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                         }
                     });
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login_Page.class));
                finish();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
    private void signIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                HomeActivity();
            }catch (ApiException e){
                Toast.makeText(getApplicationContext(),"Error code: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void HomeActivity() {
        startActivity(new Intent(getApplicationContext(), checksignin.class));
        finish();
    }
}