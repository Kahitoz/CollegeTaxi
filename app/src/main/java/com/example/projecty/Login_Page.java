package com.example.projecty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Login_Page extends AppCompatActivity {
    TextView create_account;
    EditText email;
    EditText password;

    Button login;

    ImageView google;
    ImageView facebook;
    FirebaseAuth mAuth;
    int AuthRequestCode = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        login = findViewById(R.id.appCompatButton);

        email = findViewById(R.id.email);

        password = findViewById(R.id.Password);

        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);

        mAuth = FirebaseAuth.getInstance();
        create_account = findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Sign_up.class));
            }
        });







        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_1 = email.getText().toString();
                String password_1 = password.getText().toString();
                if(email_1.length() <= 5){
                    Toast.makeText(getApplicationContext(), "email is invalid", Toast.LENGTH_LONG).show();
                }else if(password_1.length() == 0){
                    Toast.makeText(getApplicationContext(),"Password is required", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email_1, password_1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                                    startActivity(new Intent(getApplicationContext(), checksignin.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "Email Not Verified Sending confirmation link again", Toast.LENGTH_LONG).show();
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();

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


        //Google sign in starts here
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());
                Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(provider).setTosAndPrivacyPolicyUrls("https://com.example.projectz","https://com.example.projectz")
                        .setLogo(R.drawable.iconlogo).setAlwaysShowSignInMethodScreen(true).build();

                startActivityForResult(intent, AuthRequestCode);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AuthRequestCode){
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                if(Objects.requireNonNull(user.getMetadata()).getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(getApplicationContext(),"Welcome", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), checksignin.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Welcome back", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), checksignin.class));
                    finish();
                }
            }
        }else{
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(response == null){
                Toast.makeText(getApplicationContext(),"User cancelled the attempt", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.getError()).getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(getApplicationContext(), checksignin.class));
        }

    }

}