package com.example.fitnessapp;

import java.util.ArrayList;

public class FriendsListContainer {
    private ArrayList<String> friendList;
    private ArrayList<String> usernameList;
    private int friendCount;

    public FriendsListContainer(){
        friendList = new ArrayList<>();
        usernameList = new ArrayList<>();
        friendCount = 0;
    }

    public String toString(){
        String s = "";
        for(int i = 0;i<friendList.size();i++){
            s+=friendList.get(i)+" ";
        }
        return s;
    }

    public boolean addFriend(String UID, String username){
        if(UID == null || username == null){
            return false;
        }
        friendList.add(UID);
        usernameList.add(username);
        friendCount++;
        return true;
    }

    public boolean removeFriend(String UID, String username){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i).equals(UID)){
                usernameList.remove(i);
                friendList.remove(i);
                friendCount--;
                return true;
            }
        }
        //friend UID doesnt exist on their friends list
        return false;
    }

    public boolean isEmpty(){
        if(friendList == null || friendCount == 0){
            return true;
        }
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

    public ArrayList<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(ArrayList<String> usernameList) {
        this.usernameList = usernameList;
    }
}
