package com.bignerdranch.android.crmapp.Room;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sms")
public class DBSmsObject {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "sender")
    public String sender;
}
