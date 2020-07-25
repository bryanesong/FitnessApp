package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements ShopAdapter.SlistItemClickListener {
    RecyclerView shopItemsRecyclerView;
    ShopAdapter shopAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<InventoryInfoContainer.ShopItem> items = new ArrayList<>();
    final DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    final String TAG = "ShopActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        createRecyclerView();

        addFakeItems();

        retrieveInventory();

    }

    private void createRecyclerView() {
        shopItemsRecyclerView = findViewById(R.id.shopItems);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void addFakeItems() {
        ArrayList<String> shopItemsFake = new ArrayList<>();
        shopItemsFake.add("water melon hat");
        shopItemsFake.add("forbidden fruits");
        shopItemsFake.add("rgb keyboard");
        shopItemsFake.add("'gaymer rise up' t-shirt");
        shopItemsFake.add("'I'm not a simp' tank top");
        shopItemsFake.add("Ultimate Abs Vibrator");
        shopItemsFake.add("Gamer Girl Bath Water");
        shopItemsFake.add("Simp License");

        shopAdapter = new ShopAdapter(ShopActivity.this, shopItemsFake, this);
        shopAdapter.notifyDataSetChanged();
        shopItemsRecyclerView.setAdapter(shopAdapter);
    }

    private void retrieveInventory() {
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("items").exists()) {
                    Log.d(TAG, "Inventory info found");
                    //convert database object to ArrayList<TrackerData>
                    InventoryInfoContainer inventoryInfo= dataSnapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").getValue(InventoryInfoContainer.class);
                    items = inventoryInfo.getItems();


                } else {
                    Log.d(TAG, "class InventoryInfoContainer not found");

                }

                //draw list on screen
                if (items != null) {
                    Log.d(TAG, "Populate list");
                    //updateEverything();
                } else {
                    Log.d(TAG, "inventory is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "data retrieval cancelled");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(ShopActivity.this, "\"" +position + "\" item clicked",
                Toast.LENGTH_LONG).show();
    }
}