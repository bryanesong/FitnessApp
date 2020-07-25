package com.example.fitnessapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private ArrayList<ShopItem> shopItems;
    private Context mContext;
    private SlistItemClickListener mListItemClickListener;
    private ArrayList<ShopItem> boughtItems;
    final String TAG = "ShopAdapter";
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShopAdapter(Context mContext, ArrayList<ShopItem> shopItems, ArrayList<ShopItem> boughtItems, SlistItemClickListener listItemClickListener){
        this.shopItems = shopItems;
        this.mContext = mContext;
        this.boughtItems = boughtItems;
        this.mListItemClickListener = listItemClickListener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView shopItemDescription;
        public ImageView shopItemImage;
        SlistItemClickListener listItemClickListener;
        public TextView txt_seen;

        public ViewHolder(View itemView, SlistItemClickListener listItemClickListener) {
            super(itemView);
            shopItemDescription = itemView.findViewById(R.id.shopItemNameText);
            shopItemImage = itemView.findViewById(R.id.shopItemImage);
            this.listItemClickListener = listItemClickListener;
            itemView.setOnClickListener(this);
            //txt_seen = itemView.findViewById(R.id.txt_seen); //try to implement a seen function later
        }

        @Override
        public void onClick(View view) {
            listItemClickListener.onItemClick(shopItems.get(getAdapterPosition()));
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_item, parent, false);
        return new ShopAdapter.ViewHolder(view, mListItemClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ShopAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        //test if item is already bought
        if (boughtItems != null && itemAlreadyBought(position)) {
            String descriptionText = shopItems.get(position).getName() + " (bought)";
            holder.shopItemDescription.setText(descriptionText);
        } else {
            holder.shopItemDescription.setText(shopItems.get(position).getName());
        }

        //set picture to shop item image
        holder.shopItemImage.setImageResource(shopItems.get(position).getImageResource());

    }

    // Return the size of your dataset (invoked by the layout manager)

    private boolean itemAlreadyBought(int position) {
        boolean sameItemFound = false;
        if(boughtItems != null) {
            for (ShopItem item : boughtItems) {
                if (shopItems.get(position).getName().equals(item.getName())) {
                    sameItemFound = true;
                }
            }

            //set name

        }
        return sameItemFound;
    }


    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public interface SlistItemClickListener {
        void onItemClick(ShopItem curItem);
    }

}
