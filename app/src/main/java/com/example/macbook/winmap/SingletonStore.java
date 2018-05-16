package com.example.macbook.winmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

/**
 * Created by macbook on 10/05/2018.
 */

public class SingletonStore {
    private static  SingletonStore sInstance = null;

    private ArrayList <StoreModel> mListStore = new ArrayList<>();
    private boolean mCalledAlready = false;



    public static SingletonStore getInstance() {
        if (sInstance == null){
            sInstance = new SingletonStore();
        }
        return sInstance;
    }
    private SingletonStore(){
        loadStore();
    }
    public ArrayList<StoreModel> getListStore() {
        return mListStore;
    }

    public void loadStore() {
        if (!mCalledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mCalledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference storeRef = database.getReference("Store");
        storeRef.keepSynced(true);
        storeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListStore = new ArrayList<>();
                for ( DataSnapshot storeSnapshot : dataSnapshot.getChildren()){
                    StoreModel store = storeSnapshot.getValue(StoreModel.class);
                    mListStore.add(store);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}