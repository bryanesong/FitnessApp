package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {
    private ImageView itemOnPlayer_Hat,itemOnPlayer_Shirt,itemOnPlayer_Pants,itemOnPlayer_RightItem,itemOnPlayer_LeftItem,itemOnPlayer_Shoes;
    private DatabaseReference reff;
    private android.widget.GridView grid;
    private ArrayList<ShopItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initializeImageViews();//initalize all image views

        //get firebase database reference
        reff = FirebaseDatabase.getInstance().getReference();
        retrieveAndUpdateInventory(reff);

    }

    public void initializeImageViews(){
        itemOnPlayer_Hat = findViewById(R.id.itemOnPlayer_Hat);
        itemOnPlayer_Shirt = findViewById(R.id.itemOnPlayer_Shirt);
        itemOnPlayer_RightItem = findViewById(R.id.itemOnPlayer_RightItem);
        itemOnPlayer_LeftItem = findViewById(R.id.itemOnPlayer_LeftItem);
        itemOnPlayer_Pants = findViewById(R.id.itemOnPlayer_Pants);
        itemOnPlayer_Shoes = findViewById(R.id.itemOnPlayer_Shoes);
    }

    //fill up inventory with items bought by user grabbing information from database
    public void populateCurrentAccessories(DatabaseReference reference){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Hat").getValue() != null){
                    itemOnPlayer_Hat.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Hat").getValue().toString()));
                }
                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shirt").getValue() != null){
                    itemOnPlayer_Shirt.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shirt").getValue().toString()));
                }
                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Left Arm").getValue() != null){
                    itemOnPlayer_LeftItem.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Left Arm").getValue().toString()));
                }
                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Right Arm").getValue() != null){
                    itemOnPlayer_RightItem.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Right Arm").getValue().toString()));
                }
                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Pants").getValue() != null){
                    itemOnPlayer_Pants.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Pants").getValue().toString()));
                }
                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shoes").getValue() != null){
                    itemOnPlayer_Shoes.setBackgroundResource(Integer.parseInt(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shoes").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //this method will reach out and grab all iventory that the player has currently equipped
    private void retrieveAndUpdateInventory(DatabaseReference reference){
        populateCurrentAccessories(reference);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                InventoryInfoContainer inventory = snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").getValue(InventoryInfoContainer.class);
                //if inventory is empty then do nothing to populate the gridview
                if(inventory == null){
                    return;
                }
                items = inventory.getItems();
                int numItems = inventory.getItems().size();
                String[] itemName = new String[numItems];
                final int[] imageId = new int[numItems];
                for(int i = 0;i<numItems;i++){
                    itemName[i] = inventory.getItems().get(i).getName();
                    imageId[i] = inventory.getItems().get(i).getImageResource();
                }

                InventoryCustomGrid adapter = new InventoryCustomGrid(InventoryActivity.this, itemName, imageId);
                grid=findViewById(R.id.grid);
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position >= items.size()){
                            return;
                        }
                        Log.d("item type",items.get(position).getType()+"");
                        switch(items.get(position).getType()){
                            case HAT:
                                itemOnPlayer_Hat.setBackgroundResource(items.get(position).getImageResource());
                                reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Hat").setValue(items.get(position).getImageResource());
                                break;
                            case SHIRT:
                                itemOnPlayer_Shirt.setBackgroundResource(items.get(position).getImageResource());
                                reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shirt").setValue(items.get(position).getImageResource());
                                break;
                            case ARMS:
                                Log.d("left arm status",snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Left Arm").getValue()+"");
                                if(snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Left Arm").getValue() == null){
                                    itemOnPlayer_LeftItem.setBackgroundResource(items.get(position).getImageResource());
                                    reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Left Arm").setValue(items.get(position).getImageResource());
                                }else{
                                    itemOnPlayer_RightItem.setBackgroundResource(items.get(position).getImageResource());
                                    reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Right Arm").setValue(items.get(position).getImageResource());
                                }
                                break;
                            case PANTS:
                                itemOnPlayer_Pants.setBackgroundResource(items.get(position).getImageResource());
                                reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Pants").setValue(items.get(position).getImageResource());
                                break;
                            case SHOES:
                                itemOnPlayer_Shoes.setBackgroundResource(items.get(position).getImageResource());
                                reff.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").child("Current Equipped").child("Shoes").setValue(items.get(position).getImageResource());
                                break;
                            default:
                                Toast.makeText(InventoryActivity.this, "You Clicked at " +imageId[+ position]+" INVALID!", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}