package com.example.fitnessapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
This class will basically take in a JSON file and will allow you to access different variables from the JSON file in a more readible and processible format
 */
public class USDAFoodParser {

    public ArrayList<FoodEntry> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<FoodEntry> foodList) {
        this.foodList = foodList;
    }

    ArrayList<FoodEntry> foodList = new ArrayList<>();

    //overloaded constructor will take in an object assuming that it is a JSON object
    public USDAFoodParser(JSONArray o){
        JSONArray foodNutrientArray;
        JSONObject obj2,temp;
        Log.d("HAHAHAHAA",o.toString());
        try{
            Log.d("length",o.length()+"");
            for(int i =0;i<o.length();i++){
                obj2 = new JSONObject(o.getJSONObject(i).toString());
                foodNutrientArray = new JSONArray(obj2.getJSONArray("foodNutrients").toString());

                double protein = 0;
                double carbs = 0;
                double fats = 0;
                for(int j =0;j<foodNutrientArray.length();j++){
                    temp = new JSONObject(foodNutrientArray.get(j).toString());
                    if(temp.get("name").equals("Protein")){
                        protein = temp.getDouble("amount");
                    }
                    if(temp.get("name").equals("Carbohydrate, by difference")){
                        carbs = temp.getDouble("amount");
                    }
                    if(temp.get("name").equals("Total lipid (fat)")){
                        fats = temp.getDouble("amount");
                    }

                }

                foodList.add(new FoodEntry(
                        protein,
                        carbs,
                        fats,
                        obj2.get("description").toString(),
                        obj2.get("brandOwner").toString()
                ));
            }

            for(int i = 0;i<foodList.size();i++){
                Log.d("FoodList Test",foodList.get(i).toString());
            }

        }catch(Exception e){
            Log.e("USDAFoodParser","Probably not a JSONArray");
            Log.e("Exception Error",e.toString());
        }

    }

    public class FoodEntry{
        double protein,carbs,fats;
        String foodName,brandName;

        public FoodEntry(double protein, double carbs, double fats, String foodName, String brandName){
            this.protein = protein;
            this.carbs = carbs;
            this.fats = fats;
            this.foodName = foodName;
            this.brandName = brandName;
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


        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getCarbs() {
            return carbs;
        }

        public void setCarbs(double carbs) {
            this.carbs = carbs;
        }

        public double getFats() {
            return fats;
        }

        public void setFats(double fats) {
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
