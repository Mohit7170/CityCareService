package com.app.citycareservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.adapters.recycler_view.settings.AddressAdpater;
import com.app.citycareservice.activities.AddAddressActivity;
import com.app.citycareservice.utils.roomDB.AddressDatabase;

public class ManageAddressActivity extends AppCompatActivity {

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
                startActivity(new Intent(activity,AddAddressActivity.class));
//                new AddAddressActivity().show(getSupportFragmentManager(), "AddAddressBottomFrag");
//                new AddAddressActivity(activity).show();
            }
        });

    }

    @Override
    protected void onStart() {
        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
        addressAdpater.setData(addressDatabase.addressDAO().getAddresses());
        super.onStart();
    }

}