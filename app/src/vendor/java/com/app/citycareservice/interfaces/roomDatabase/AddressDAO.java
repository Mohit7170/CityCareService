package com.app.citycareservice.interfaces.roomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.citycareservice.modals.AddressModal.AddressModal;

import java.util.List;

@Dao
public interface AddressDAO {

    @Query("Select * from AddressDB")
    List<AddressModal> getAddresses();

    @Query("Select * from AddressDB WHERE id = :value")
    AddressModal getAddress(int value);

    @Query("SELECT EXISTS (SELECT 1 FROM AddressDB WHERE :key = :value)")
    boolean addressExists(String key, String value);

    @Insert
    void insertAddress(AddressModal address);

    @Update
    void updateAddress(AddressModal address);

    @Delete
    void deleteAddress(AddressModal address);

}
