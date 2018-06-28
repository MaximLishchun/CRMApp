package com.bignerdranch.android.crmapp.Room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SmsDao {

    @Insert
    void saveSms(DBSmsObject smsObject);

    @Delete
    void deleteSms(DBSmsObject smsObject);

    @Query("SELECT * FROM sms")
    List<DBSmsObject> getAllSms();
}
