package com.example.basicactivity.extActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.basicactivity.data.Item;
import com.example.basicactivity.databinding.SelectorBinding;
import com.example.basicactivity.tiles.ItemClickListener;
import com.example.basicactivity.tiles.TilesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Selector extends AppCompatActivity {

    private List<Item> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectorBinding binding = SelectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle=getIntent().getExtras();
//        String task=bundle.getString("task");
//        String title=bundle.getString("title");
        try {
            JSONArray arr = new JSONArray(bundle.getString("strJSONdata"));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonObject = (JSONObject) arr.get(i);
                data.add(new Item(jsonObject.getString("id"), jsonObject.getString("name")));
            }
            //itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();

        }

        binding.recyclerView.setAdapter(
                new TilesAdapter(
                        data, new ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Item item = data.get(position);
                                Toast.makeText(getApplicationContext(), "ID: " + item.getId() + ", Name: " + item.getName(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.putExtra("item", item);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }
                ));
    }
}