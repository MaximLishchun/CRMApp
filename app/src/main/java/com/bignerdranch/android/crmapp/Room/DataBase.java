package com.bignerdranch.android.crmapp.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DBSmsObject.class}, version = 1)
public abstract class  DataBase extends RoomDatabase {

    public abstract SmsDao getSmsDao();

}
