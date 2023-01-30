package com.example.projecty;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Dashboard extends AppCompatActivity  {

TextView date, name;
TextView time;
ImageView home, message, person, setting;
ConstraintLayout carpool, auto,whole_cab;
FirebaseAuth mAuth;
FirebaseFirestore data_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        name = findViewById(R.id.name);
        Calendar calendar = Calendar.getInstance();
        String current_date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(current_date);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        assert uid!=null;
        data_base = FirebaseFirestore.getInstance();
        data_base.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name_1= task.getResult().getString("name");
                    name.setText(name_1);

                }
            }
        });



        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                String date = simpleDateFormat.format(calendar.getTime());
                                time.setText(date);
                            }
                        });
                    }
                }catch (Exception e){

                }
            }
        };
        thread.start();

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
        carpool = findViewById(R.id.carpool);
        auto = findViewById(R.id.auto);
        whole_cab = findViewById(R.id.whole_cab);
        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.projecty.carpool.class));
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.projecty.auto.class));
            }
        });
        whole_cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), wholecab.class));
            }
        });




    }
}