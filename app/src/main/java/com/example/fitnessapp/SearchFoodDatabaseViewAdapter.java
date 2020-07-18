package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFoodDatabaseViewAdapter extends RecyclerView.Adapter<SearchFoodDatabaseViewAdapter.ViewHolder> {

    public SearchFoodDatabaseViewAdapter(ArrayList<USDAFoodParser.FoodEntry> foodsInDatabase, Context SFDContext) {
        this.foodsInDatabase = foodsInDatabase;
        this.SFDContext = SFDContext;
    }

    private ArrayList<USDAFoodParser.FoodEntry> foodsInDatabase= new ArrayList<>();
    private Context SFDContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_food_database_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.FoodName.setText(foodsInDatabase.get(position).getFoodName());
        USDAFoodParser.FoodEntry curEntry = foodsInDatabase.get(position);
        holder.OtherInfo.setText("Brand: " + curEntry.getBrandName() + " \nCalories: " + curEntry.getCalories() + "   Protein: " + curEntry.getProtein() + "g   Fats: " + curEntry.getFats() + "g   Carbs: " + curEntry.getCarbs() + "g");
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SFDContext, "Clicked on item " + (position + 1), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return foodsInDatabase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView FoodName, OtherInfo;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FoodName = itemView.findViewById(R.id.SFDFoodName);
            OtherInfo = itemView.findViewById(R.id.SFDOtherInfo);
            parentLayout = itemView.findViewById(R.id.SFD_RecyclerView_layout);
        }
    }

}
