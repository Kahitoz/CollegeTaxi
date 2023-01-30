package com.example.projecty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class carpool extends AppCompatActivity {
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
        setContentView(R.layout.activity_carpool);
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
        data_base.collection("carpool").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
class getset_1{
String address, destination;

    public getset_1() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}


class profileFragmentAdapter extends RecyclerView.Adapter<profileFragmentAdapter.pfHolder>{
    Context pf;
    ArrayList<getset_1> qualityArrayList;

    public profileFragmentAdapter(Context pf, ArrayList<getset_1> qualityArrayList) {
        this.pf = pf;
        this.qualityArrayList = qualityArrayList;
    }

    @NonNull
    @Override
    public profileFragmentAdapter.pfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(pf).inflate(R.layout.destination_holder, parent, false);

        return new pfHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull profileFragmentAdapter.pfHolder holder, int position) {
        getset_1 data = qualityArrayList.get(position);
        holder.address.setText(data.address);
        holder.destination.setText(data.destination);
    }

    @Override
    public int getItemCount() {
        return qualityArrayList.size();
    }
    public static class pfHolder extends RecyclerView.ViewHolder{
      TextView address, destination;
        public pfHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            destination = itemView.findViewById(R.id.destination);

        }
    }
}