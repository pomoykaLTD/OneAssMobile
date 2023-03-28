package com.example.basicactivity.tiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicactivity.R;
import com.example.basicactivity.data.Item;

import java.util.List;

public class TilesAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> itemList;
    private ItemClickListener itemClickListener;

    public TilesAdapter(List<Item> itemList, ItemClickListener itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selector_item, parent, false);
        return new ItemViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
