package com.example.fitnessapp;

import java.util.ArrayList;

public class FriendsListContainer {
    private ArrayList<String> friendList;
    private int friendCount;

    public FriendsListContainer(){
        friendList = new ArrayList<>();
        friendCount = 0;
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
        friendCount++;
    }

    public boolean removeFriend(String UID){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i).equals(UID)){
                friendList.remove(i);
                friendCount--;
                return true;
            }
        }
        //friend UID doesnt exist on their friends list
        return false;
    }

    public boolean isEmpty(){
        return friendList.isEmpty();
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }
}
