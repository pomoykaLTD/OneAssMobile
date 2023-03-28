package com.example.basicactivity.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item implements Parcelable {
    private String id;
    private String name;
    public Item(String id,String name){
        this.id=id;
        this.name=name;
    }

    public Item(String barCode){
        //парсим результат сканирования в JSON

        int startIndex = barCode.indexOf("{");
        int endIndex = barCode.lastIndexOf("}");
        if(startIndex>0 && endIndex>0){
            String json = barCode.substring(startIndex, endIndex+1);
            try {
                JSONObject jsonObject=new JSONObject(json);
                this.id=jsonObject.getString("ID");
                this.name=jsonObject.getString("Name");
                //артикул=jsonObject.getString("ART");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            //this.id=null;
            this.name="Неопределено";
        }
    }

    protected Item(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId(){return id;}
    public String getName(){return name;}
    public String toString(){return name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id) && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getIdToJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("id",getId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json.toString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}

