package com.example.fitnessapp;

import java.util.ArrayList;

public class FriendsListContainer {

    private ArrayList<String> friendList = new ArrayList<>();

    public FriendsListContainer(){

    }

    public String toString(){
        String s = "";
        for(int i = 0;i<friendList.size();i++){
            s+=friendList.get(i)+" ";
        }
        return s;
    }

    public void addFriend(String UID){
        friendList.add(UID);
    }

    public boolean removeFriend(String UID){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i).equals(UID)){
                friendList.remove(i);
                return true;
            }
        }
        //friend UID doesnt exist on their friends list
        return false;
    }
}
