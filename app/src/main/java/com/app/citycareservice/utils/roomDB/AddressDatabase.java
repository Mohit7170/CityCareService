package com.app.citycareservice.utils.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.citycareservice.interfaces.roomDatabase.AddressDAO;
import com.app.citycareservice.modals.AddressModal.AddressModal;

@Database(entities = AddressModal.class, exportSchema = false, version = 1)
public abstract class AddressDatabase extends RoomDatabase {

    private static final String DB_NAME = "AddressDatabase";
    private static AddressDatabase instance;

    public static synchronized AddressDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AddressDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AddressDAO addressDAO();
}
