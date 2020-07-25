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

public class ShopActivity extends AppCompatActivity implements ShopAdapter.SlistItemClickListener, ShopBuyDialog.clickListener {
    RecyclerView shopItemsRecyclerView;
    ShopAdapter shopAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ShopItem> items = new ArrayList<>();
    final DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    final String TAG = "ShopActivity";

    final String WATERMELON_HAT_DESCRIPTION = "";
    final String FORBIDDEN_FRUITS_DESCRIPTION = "";
    final String RGB_KEYBOARD_DESCRIPTION = "";
    final String GAYMER_RISE_UP_TSHIRT_DESCRIPTION ="";
    final String IM_NOT_A_SIMP_TANK_TOP_DESCRIPTION = "";
    final String ULTIMATE_ABS_VIBRATOR_DESCRIPTION = "";
    final String GAMER_GIRL_BATH_WATER_DESCRIPTION = "Is gamer girl bath water wet?";
    final String SIMP_LICENSE_DESCRIPTION = "This is one of the most treasured items one could possibly hope to get their grubby hands on. 'I've finally done it!' - David Yip after 21 years of simping. ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        retrieveInventory();

        createRecyclerView();



    }

    private void createRecyclerView() {
        shopItemsRecyclerView = findViewById(R.id.shopItems);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void addFakeItems() {
        ArrayList<ShopItem> shopItemsFake = new ArrayList<>();
        shopItemsFake.add(new ShopItem("Water Melon Hat", 1, 100, WATERMELON_HAT_DESCRIPTION, R.drawable.watermelon_hat));
        shopItemsFake.add(new ShopItem("Forbidden Fruits", 3, 100, FORBIDDEN_FRUITS_DESCRIPTION, R.drawable.forbidden_fruits));
        shopItemsFake.add(new ShopItem("RGB Keyboard", 3, 100, RGB_KEYBOARD_DESCRIPTION, R.drawable.rbg_keyboard));
        shopItemsFake.add(new ShopItem("'Gaymers Rise Up' T-Shirt", 2, 100, GAYMER_RISE_UP_TSHIRT_DESCRIPTION, R.drawable.gaymers_rise_up));
        shopItemsFake.add(new ShopItem("'I'm Not A Simp' Tank Top", 2 ,100, IM_NOT_A_SIMP_TANK_TOP_DESCRIPTION, R.drawable.simp_tank_top));
        shopItemsFake.add(new ShopItem("Ultimate Abs Vibrator", 2, 100, ULTIMATE_ABS_VIBRATOR_DESCRIPTION, R.drawable.ab_vibrator));
        shopItemsFake.add(new ShopItem("Gamer Girl Bath Water", 3, 100, GAMER_GIRL_BATH_WATER_DESCRIPTION, R.drawable.gamer_juice));
        shopItemsFake.add(new ShopItem("Simp License", 3, 100, SIMP_LICENSE_DESCRIPTION, R.drawable.simp_card));
        shopAdapter = new ShopAdapter(ShopActivity.this, shopItemsFake, items, this);
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

                } else {
                    Log.d(TAG, "inventory is null");
                }
                addFakeItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "data retrieval cancelled");
            }
        });
    }

    @Override
    public void onItemClick(ShopItem curItem) {
        //open dialog when item is clicked
        ShopBuyDialog dialog = new ShopBuyDialog(this, curItem);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void cancelItem() {
        Log.d(TAG, "cancelled");
    }

    @Override
    public void buyItem(ShopItem curItem) {
        Log.d(TAG, "bought");
        boolean sameItemFound = false;
        for(ShopItem item : items) {
            if(curItem.getName().equals(item.getName())) {
                sameItemFound = true;
            }
        }
        if(!sameItemFound) {
            items.add(curItem);
            reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("items").setValue(items);
            Toast.makeText(this, "" + curItem.getName() + " bought!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Item already bought!",
                    Toast.LENGTH_LONG).show();
        }
    }
}