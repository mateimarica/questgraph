package com.questgraph.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class, Authorization.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDAO accountDAO();
    public abstract AuthorizationDAO authorizationDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                         AppDatabase.class, "Database").build();
    }
}





