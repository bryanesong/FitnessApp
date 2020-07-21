package com.example.fitnessapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;

public class SearchFoodDatabaseDialog extends AppCompatDialogFragment {
    private EditText servingInput;
    clickListener mCLickListener;
    USDAFoodParser.FoodEntry curEntry;
    TextView dialogServingText, dialogOtherInfoText, dialogBrandText;
    View view;

    public SearchFoodDatabaseDialog(clickListener mClickListener, USDAFoodParser.FoodEntry curEntry) {
        this.mCLickListener = mClickListener;
        this.curEntry = curEntry;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //LayoutInflater inflater = getActivity().getLayoutInflater();

        view = getLayoutInflater().inflate(R.layout.search_food_database_dialog_layout, null);

        createInput();

        createTextViews();

        setTextView("1");



        builder.setView(view)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCLickListener.cancelItem();
                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCLickListener.addItem(servingInput.getText().toString(), curEntry);
                    }
                });

        return builder.create();
    }

    private void createInput() {
        servingInput = (EditText)view.findViewById(R.id.SFDDialogServingInput);
        servingInput.setText("1");
        servingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("SFDDialog", "changed to: " + charSequence);
                setTextView("" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void createTextViews() {
        dialogServingText = (TextView)view.findViewById(R.id.SFDDialogServingText);
        dialogOtherInfoText = (TextView)view.findViewById(R.id.SFDDialogOtherInfo);
        dialogBrandText = (TextView)view.findViewById(R.id.SFDDialogBrand);

        if(!curEntry.getFoodName().equals("No brand owner.")) {
            dialogBrandText.setText(curEntry.getBrandName());
        } else {
            dialogBrandText.setText(curEntry.getFoodName());
        }

    }

    private void setTextView(String servingSize) {
        if(!servingSize.equals("")) {
            int servings = Integer.parseInt(servingSize);
            String otherInfoText = "Calories: " + servings*curEntry.getCalories() + "   Protein: " + servings*curEntry.getProtein() + "g   Fats: " + servings*curEntry.getFats() + "g   Carbs: " + servings*curEntry.getCarbs() + "g";
            dialogOtherInfoText.setText(otherInfoText);
            if(servings == 1 || servings == 0) {
                dialogServingText.setText("Serving (100 grams each)");
            } else {
                dialogServingText.setText("Servings (100 grams each)");
            }

        }

    }

    public interface clickListener{
        void cancelItem();
        void addItem(String servingSize, USDAFoodParser.FoodEntry curEntry);
    }
}
