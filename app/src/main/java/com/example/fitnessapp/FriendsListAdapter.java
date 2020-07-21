package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {
    private String[] mDataset;
    private friendClicked mFriendClick;
    private Context FLA;
    private ArrayList<String> friends;

    //confirm class implements interface
    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendsListAdapter(Context FLA, friendClicked mFriendClick, ArrayList<String> friends) {
        this.FLA = FLA;
        this.mFriendClick = mFriendClick;
        this.friends = friends;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView textView;

        private friendClicked friendClick;

        public MyViewHolder(TextView v, friendClicked friendClick) {
            super(v);
            this.friendClick = friendClick;
            textView = v;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            friendClick.clicked(getAdapterPosition());
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public FriendsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        MyViewHolder vh = new MyViewHolder(v, mFriendClick);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(friends.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return friends.size();
    }

    public interface friendClicked {
        void clicked(int position);
    }

}
