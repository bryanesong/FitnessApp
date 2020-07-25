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
    ArrayList<InventoryInfoContainer.ShopItem> items = new ArrayList<>();
    final DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    final String TAG = "ShopActivity";

    final String WATERMELON_HAT_DESCRIPTION = "";
    final String FORBIDDEN_FRUITS_DESCRIPTION = "";
    final String RGB_KEYBOARD_DESCRIPTION = "";
    final String GAYMER_RISE_UP_TSHIRT_DESCRIPTION ="";
    final String IM_NOT_A_SIMP_TANK_TOP_DESCRIPTION = "";
    final String ULTIMATE_ABS_VIBRATOR_DESCRIPTION = "";
    final String GAMER_GIRL_BATH_WATER_DESCRIPTION = "";
    final String SIMP_LICENSE_DESCRIPTION = "This is one of the most treasured items one could possibly hope to get their grubby hands on. 'I've finally done it!' - David Yip after 21 years of simping. ";

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
        InventoryInfoContainer container = new InventoryInfoContainer(new ArrayList<InventoryInfoContainer.ShopItem>());
        container.addShopItem("water melon hat", 1, 100, WATERMELON_HAT_DESCRIPTION, R.drawable.watermelon_hat);
        container.addShopItem("forbidden fruits", 3, 100, FORBIDDEN_FRUITS_DESCRIPTION, R.drawable.forbidden_fruits);
        container.addShopItem("rgb keyboard", 3, 100, RGB_KEYBOARD_DESCRIPTION, R.drawable.rbg_keyboard);
        container.addShopItem("'gaymers rise up' t-shirt", 2, 100, GAYMER_RISE_UP_TSHIRT_DESCRIPTION, R.drawable.gaymers_rise_up);
        container.addShopItem("'I'm not a simp' tank top", 2 ,100, IM_NOT_A_SIMP_TANK_TOP_DESCRIPTION, R.drawable.simp_tank_top);
        container.addShopItem("Ultimate Abs Vibrator", 2, 100, ULTIMATE_ABS_VIBRATOR_DESCRIPTION, R.drawable.ab_vibrator);
        container.addShopItem("Gamer Girl Bath Water", 6, 100, GAMER_GIRL_BATH_WATER_DESCRIPTION, R.drawable.gamer_juice);
        container.addShopItem("Simp License", 6, 100, SIMP_LICENSE_DESCRIPTION, R.drawable.simp_card);

        ArrayList<InventoryInfoContainer.ShopItem> shopItemsFake = container.getItems();

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
    public void onItemClick(InventoryInfoContainer.ShopItem curItem) {
        //open dialog when item is clicked
        ShopBuyDialog dialog = new ShopBuyDialog(this, curItem);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void cancelItem() {
        Log.d(TAG, "cancelled");
    }

    @Override
    public void buyItem(InventoryInfoContainer.ShopItem curItem) {
        Log.d(TAG, "bought");
        items.add(curItem);
        reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("items").setValue(items);
    }
}