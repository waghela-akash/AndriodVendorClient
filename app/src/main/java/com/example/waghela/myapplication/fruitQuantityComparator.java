package com.example.waghela.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by waghela on 24/9/16.
 */
class fruitQuantityComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1,JSONObject o2){
        double v1=0,v2=0;
        try {
            v1 = o1.getDouble("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            v2 = o2.getDouble("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Double.compare(v1,v2);
    }
}
