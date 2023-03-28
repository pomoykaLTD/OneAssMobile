package com.example.basicactivity.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

public class Item_Nimenclatula extends Item{
    private String artikul;
    public Item_Nimenclatula(String id, String name, String artikul) {
        super(id, name);
        this.artikul=artikul;
    }

    public Item_Nimenclatula(String barCode) {
        super(barCode);

        //парсим результат сканирования в JSON
        if(((Item)this).getId()!=null){
            int startIndex = barCode.indexOf("{");
            int endIndex = barCode.lastIndexOf("}");
            if(startIndex>0 && endIndex>0){
                String json = barCode.substring(startIndex, endIndex+1);
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    this.artikul=jsonObject.getString("ART");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Item_Nimenclatula(Parcel in) {
        super(in);
    }
}
