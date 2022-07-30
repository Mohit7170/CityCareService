package com.app.citycareservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.dialogs.bottomSheet.AddAddressBottomSheet;
import com.app.citycareservice.adapters.recycler_view.settings.AddressAdpater;
import com.app.citycareservice.interfaces.sheetDismissListner;
import com.app.citycareservice.utils.roomDB.AddressDatabase;

public class ManageAddressActivity extends AppCompatActivity implements sheetDismissListner {

    private Activity activity = ManageAddressActivity.this;

    private ImageView back_iv;
    private TextView add_address_tv;
    private RecyclerView addresses_rv;

    private AddressAdpater addressAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        back_iv = findViewById(R.id.back_iv);
        add_address_tv = findViewById(R.id.add_address_tv);
        addresses_rv = findViewById(R.id.addresses_rv);

        addressAdpater = new AddressAdpater(activity);
        addresses_rv.setAdapter(addressAdpater);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(activity,AddAddressBottomSheet.class));
                AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet(ManageAddressActivity.this);
                addAddressBottomSheet.show(getSupportFragmentManager(), "AddAddressBottomFrag");

//                new AddAddressBottomSheet().show(getSupportFragmentManager(), "AddAddressBottomFrag");
//                new AddAddressBottomSheet(activity).show();
            }
        });

    }

    @Override
    protected void onStart() {
        setData();
        super.onStart();
    }


    @Override
    public void setData() {
        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
        addressAdpater.setData(addressDatabase.addressDAO().getAddresses());
    }
}