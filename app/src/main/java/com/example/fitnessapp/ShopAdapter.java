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
    private ArrayList<String> shopItems;
    private Context mContext;
    private SlistItemClickListener mListItemClickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShopAdapter(Context mContext, ArrayList<String> shopItems, SlistItemClickListener listItemClickListener){
        this.shopItems = shopItems;
        this.mContext = mContext;
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
            listItemClickListener.onItemClick(getAdapterPosition());
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

        Log.d("Shop items",shopItems.size()+"");
        Log.d("shop desc", ""+(holder.shopItemDescription == null));
        holder.shopItemDescription.setText(shopItems.get(position));
        //set picture to shop item image
        if(shopItems.get(position).equals("water melon hat")){
            holder.shopItemImage.setImageResource(R.drawable.watermelon_hat);
        }else if(shopItems.get(position).equals("forbidden fruits")){
            holder.shopItemImage.setImageResource(R.drawable.forbidden_fruits);
        }else if(shopItems.get(position).equals("rgb keyboard")){
            holder.shopItemImage.setImageResource(R.drawable.rbg_keyboard);
        }else if(shopItems.get(position).equals("'gaymer rise up' t-shirt")){
            holder.shopItemImage.setImageResource(R.drawable.gaymers_rise_up);
        }else if(shopItems.get(position).equals("'I'm not a simp' tank top")){
            holder.shopItemImage.setImageResource(R.drawable.simp_tank_top);
        }else if(shopItems.get(position).equals("Ultimate Abs Vibrator")){
            holder.shopItemImage.setImageResource(R.drawable.ab_vibrator);
        }else if(shopItems.get(position).equals("Gamer Girl Bath Water")){
            holder.shopItemImage.setImageResource(R.drawable.gamer_juice);
        }else if(shopItems.get(position).equals("Simp License")){
            holder.shopItemImage.setImageResource(R.drawable.simp_card);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public interface SlistItemClickListener {
        void onItemClick(int position);
    }
}
