package com.example.projecty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class add_name extends AppCompatActivity {
    EditText username;
    Button next;
    FirebaseFirestore data_base;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
        username = findViewById(R.id.editText);
        data_base = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        next = findViewById(R.id.appCompatButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                if(name.length() < 1){
                    username.setError("Enter a valid username");
                }else{
                    String uid = mAuth.getUid();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", name);
                    assert uid != null;
                    data_base.collection("users").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Account Created Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Sign_up.class));
                            finish();
                        }
                    });

                }

                }
        });

    }

}