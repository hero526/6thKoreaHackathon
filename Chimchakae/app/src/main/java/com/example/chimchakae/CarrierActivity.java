package com.example.chimchakae;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chimchakae.Model.Follower;
import com.example.chimchakae.View.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarrierActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ListView listView;
    private ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier);



        listView = findViewById(R.id.lv_follower);
        adapter = new ListViewAdapter();



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("followers");


        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDatabaseReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                adapter.clear();

                                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                    Follower follower = userSnapshot.getValue(Follower.class);
                                    Log.d("Followers", "Followers" + follower.getUserID());
                                    adapter.addItem(follower.getUserID(), follower.getCarNum());
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();


        //TODO: listView.setOnClickListener() 함수 짜기


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mDatabaseReference.removeEventListener(mChildEventListener);
    }
}
