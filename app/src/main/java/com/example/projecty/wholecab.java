package com.example.projecty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class wholecab extends AppCompatActivity {
    ImageView home, message, person, setting;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseFirestore data_base;
    profileFragmentAdapter adapter;
    ArrayList<getset_1> qualityArrayList;
    ProgressBar hint;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholecab);
        home = findViewById(R.id.home);
        message = findViewById(R.id.message);
        person = findViewById(R.id.person);
        setting = findViewById(R.id.setting);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycle);
        mAuth = FirebaseAuth.getInstance();
        data_base = FirebaseFirestore.getInstance();
        qualityArrayList = new ArrayList<getset_1>();
        hint = findViewById(R.id.hint);
        adapter = new profileFragmentAdapter(getApplicationContext(),qualityArrayList);
        data_base.collection("wholecab").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    return;
                }
                for(DocumentChange dc:value.getDocumentChanges()){
                    qualityArrayList.add(dc.getDocument().toObject(getset_1.class));
                    hint.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




    }
}