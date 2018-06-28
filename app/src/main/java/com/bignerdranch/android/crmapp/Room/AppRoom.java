package com.bignerdranch.android.crmapp.Room;


import android.app.Application;
import android.arch.persistence.room.Room;

public class AppRoom extends Application {

    public static AppRoom instance;

    private DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = Room.databaseBuilder(this, DataBase.class, "sms").build();
    }

    public  static AppRoom getInstance(){
        return instance;
    }

    public DataBase getDataBase(){
        return dataBase;
    }
}