package com.example.fitnessapp;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

import java.io.BufferedInputStream;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/*
This class will basically take in a JSON file and will allow you to access different variables from the JSON file in a more readible and processible format
 */
public class USDAFoodParser {
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";
    String s = "";
    ArrayList<FoodEntry> foodList = new ArrayList<>();

    public ArrayList<FoodEntry> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<FoodEntry> foodList) {
        this.foodList = foodList;
    }

    public USDAFoodParser() {

    }
    //overloaded constructor will take in an object assuming that it is a JSON object
    public USDAFoodParser(JSONArray o){
        JSONArrayParser(o);
    }

    public ArrayList<FoodEntry> JSONArrayParser(JSONArray o) {
        JSONArray foodNutrientArray;
        JSONObject obj2,temp;
        Log.d("HAHAHAHAA",o.toString());
        try{
            Log.d("length",o.length()+"");
            for(int i =0;i<o.length();i++){

                obj2 = new JSONObject(o.getJSONObject(i).toString());
                foodNutrientArray = new JSONArray(obj2.getJSONArray("foodNutrients").toString());

                int protein = 0;
                int carbs = 0;
                int fats = 0;
                for(int j =0;j<foodNutrientArray.length();j++){
                    temp = new JSONObject(foodNutrientArray.get(j).toString());
                    if(temp.get("name").equals("Protein")){
                        protein = (int)temp.getDouble("amount");
                    }
                    if(temp.get("name").equals("Carbohydrate, by difference")){
                        carbs = (int)temp.getDouble("amount");
                    }
                    if(temp.get("name").equals("Total lipid (fat)")){
                        fats = (int)temp.getDouble("amount");
                    }

                }
                String description = "";
                if(obj2.has("description")) {
                    description = obj2.get("description").toString();
                }

                String brandOwner = "";
                if(obj2.has("brandOwner")) {
                    brandOwner = obj2.get("brandOwner").toString();
                } else {
                    brandOwner = "No brand owner.";
                }

                foodList.add(new FoodEntry(
                        protein,
                        carbs,
                        fats,
                        description,
                        brandOwner
                ));
            }

            for(int i = 0;i<foodList.size();i++){
                Log.d("FoodList Test",foodList.get(i).toString());
            }

        }catch(Exception e){
            Log.e("USDAFoodParser","Probably not a JSONArray");
            Log.e("Exception Error",e.toString());
        }
        return foodList;
    }

    public ArrayList<FoodEntry> searchFood(final String nameOfFood) {
        final ArrayList<FoodEntry> foundEntries = new ArrayList<>();
        AsyncTask asyncTask = new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Log.d("test","https://api.nal.usda.gov/fdc/v1/foods/list?api_key="+apiKey+"&query="+nameOfFood.toString().replace(" ","%20"));
                Request request = new Request.Builder()
                        .url("https://api.nal.usda.gov/fdc/v1/foods/list?api_key="+apiKey+"&query="+nameOfFood.toString().replace(" ","%20"))
                        .build();

                Response response = null;

                try{
                    response = client.newCall(request).execute();
                    return response.body().string();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o){
                //JSONText.setText(o.toString());
                //testing parsing JSON file.
                try {

                    JSONArray obj = new JSONArray(o.toString());
                    Log.d("test",obj.toString());
                    foodList = JSONArrayParser(obj);
                    Log.d("USDAFoodParser1", "" + foodList.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
        Log.d("USDAFoodParser2", "" + foodList.size());
        return foodList;
    }


    public class FoodEntry{
        int protein;
        int carbs;
        int fats;
        int calories;
        String foodName;
        String brandName;

        public FoodEntry(int protein, int carbs, int fats, String foodName, String brandName){
            this.protein = protein;
            this.carbs = (int)carbs;
            this.fats = fats;
            this.foodName = foodName;
            this.brandName = brandName;
            //"The calories in food come from carbohydrates, proteins, and fats. A gram of carbohydrate contains 4 calories. A gram of protein also contains 4 calories. A gram of fat, though, contains 9 calories"
            this.calories = (protein * 4) + (carbs * 4) + (fats * 9);
        }

        @Override
        public String toString(){
            String s = "";
            s+="foodName: "+foodName+"\n";
            s+="brandName: "+brandName+"\n";
            s+="protein: "+protein+"\n";
            s+="carbs: "+carbs+"\n";
            s+="fats: "+fats+"\n";
            return s;
        }
        public int getCalories() {
            return calories;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public int getProtein() {
            return protein;
        }

        public void setProtein(int protein) {
            this.protein = protein;
        }

        public int getCarbs() {
            return carbs;
        }

        public void setCarbs(int carbs) {
            this.carbs = (int)carbs;
        }

        public int getFats() {
            return fats;
        }

        public void setFats(int fats) {
            this.fats = fats;
        }

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }


    }
}
