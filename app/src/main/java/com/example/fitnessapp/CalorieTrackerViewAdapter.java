package com.example.fitnessapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalorieTrackerViewAdapter extends RecyclerView.Adapter<CalorieTrackerViewAdapter.ViewHolder> {
    private final int REGULAR_ENTRY = 0;
    private final int DATE_ENTRY = 1;

    private ArrayList<TrackerData> entries= new ArrayList<>();
    private Context SFDContext;
    private CTlistItemClickListener mListItemClickListener;

    public CalorieTrackerViewAdapter(Context SFDContext, ArrayList<TrackerData> entries, CTlistItemClickListener listItemClickListener) {
        this.entries = entries;
        this.SFDContext = SFDContext;
        this.mListItemClickListener = listItemClickListener;
        setHasStableIds(true);
    }

    public void loadEntriesList(ArrayList<TrackerData> entries) {
        this.entries.clear();
        if (entries != null) {
            this.entries = entries;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == REGULAR_ENTRY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            ViewHolder holder = new ViewHolder(view, mListItemClickListener);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calorie_tracker_date_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TrackerData curEntry = entries.get(position);
        if(holder.getItemViewType() == DATE_ENTRY) {
            holder.DateText.setText(curEntry.getDateText());
        } else {
            holder.CalorieText.setText("" + curEntry.getCalories() + " cal");
            holder.FoodText.setText(curEntry.getFoodType());
            holder.OtherInfoText.setText("" + curEntry.getQuantity() + " " + curEntry.getMeasurement());

        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView CalorieText, FoodText, OtherInfoText, DateText;
        RelativeLayout parentLayout;
        CTlistItemClickListener listItemClickListener;


        //regular entry view holder
        public ViewHolder(@NonNull View itemView, CTlistItemClickListener listItemClickListener) {
            super(itemView);
            CalorieText = itemView.findViewById(R.id.calorieText);
            FoodText = itemView.findViewById(R.id.foodTypeText);
            OtherInfoText = itemView.findViewById(R.id.otherText);
            parentLayout = itemView.findViewById(R.id.CTrecyclerView);

            this.listItemClickListener = listItemClickListener;
            itemView.setOnClickListener(this);
        }

        //date entry view holder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DateText = itemView.findViewById(R.id.CTdateTextBox);
            parentLayout = itemView.findViewById(R.id.CTrecyclerView);
        }


        @Override
        public void onClick(View view) {
            int realIndex = getAdapterPosition();
            for(int i = 0; i < getAdapterPosition(); i++) {
                if(entries.get(i).isDateData()) {
                    realIndex--;
                }
            }
            listItemClickListener.onItemClick(realIndex);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(entries.get(position).isDateData()) {
            return DATE_ENTRY;
        } else {
            return REGULAR_ENTRY;
        }
    }
    //interface to implement item listener in SFD activity.
    public interface CTlistItemClickListener{
        void onItemClick(int position);
    }


}
