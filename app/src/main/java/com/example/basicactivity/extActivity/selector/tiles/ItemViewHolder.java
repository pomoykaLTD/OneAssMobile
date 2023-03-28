package com.example.basicactivity.tiles;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicactivity.R;
import com.example.basicactivity.data.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewName;
    private ItemClickListener itemClickListener;

    public ItemViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.tvTitle);
        this.itemClickListener = itemClickListener;
        itemView.setOnClickListener(this);
    }

    public void bindData(Item item) {
        textViewName.setText(item.getName());
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onItemClick(view, getAdapterPosition());
    }

}
