package com.example.fitnessapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SearchFoodDatabaseDialog extends AppCompatDialogFragment {
    private EditText servingInput;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater infalter = getActivity().getLayoutInflater();
        View view = getLayoutInflater().inflate(R.layout.search_food_database_dialog_layout, null);

        builder.setView(view)
                .setTitle("Food")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        servingInput = view.findViewById(R.id.SFDDialogServingInput);

        return builder.create();
    }
}
