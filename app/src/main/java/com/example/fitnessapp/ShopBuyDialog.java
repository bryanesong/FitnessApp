package com.example.fitnessapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ShopBuyDialog extends AppCompatDialogFragment {
    private TextView itemText, itemDescription;
    private ImageView itemImage;
    private InventoryInfoContainer.ShopItem curItem;
    private ShopBuyDialog.clickListener mClickListener;
    private View view;

    public ShopBuyDialog(ShopBuyDialog.clickListener mClickListener, InventoryInfoContainer.ShopItem curItem) {
        this.mClickListener = mClickListener;
        this.curItem = curItem;
    }

    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getLayoutInflater().inflate(R.layout.shop_buy_dialog_layout, null);

        createTextViews();

        createImageView();

        builder.setView(view)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mClickListener.cancelItem();
                    }
                })
                .setPositiveButton("BUY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mClickListener.buyItem(curItem);
                    }
                });

        return builder.create();
    }

    private void createTextViews() {
        itemText = (TextView)view.findViewById(R.id.SBitemText);
        itemText.setText(curItem.getName());

        itemDescription = (TextView)view.findViewById(R.id.SBitemDescription);
        itemDescription.setText(curItem.getDescription());
    }

    private void createImageView() {
        itemImage = (ImageView)view.findViewById(R.id.SBitemImage);
        itemImage.setImageResource(curItem.getImageResource());
    }


    public interface clickListener{
        void cancelItem();
        void buyItem(InventoryInfoContainer.ShopItem curItem);
    }
}
