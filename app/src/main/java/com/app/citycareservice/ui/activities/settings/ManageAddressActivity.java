package com.app.citycareservice.ui.activities.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.adapters.recycler_view.settings.AddressAdpater;
import com.app.citycareservice.ui.dialogs.bottomSheet.AddAddressBottomSheet;
import com.app.citycareservice.utils.roomDB.AddressDatabase;

public class ManageAddressActivity extends AppCompatActivity {

    private final Activity activity = ManageAddressActivity.this;

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

        back_iv.setOnClickListener(v -> onBackPressed());

        add_address_tv.setOnClickListener(v -> {
            AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet(this::setDataInRv);
            addAddressBottomSheet.show(getSupportFragmentManager(), "AddAddressBottomFrag");
        });

    }

    @Override
    protected void onStart() {
        setDataInRv();
        super.onStart();
    }

    private void setDataInRv() {
        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
        addressAdpater.setData(addressDatabase.addressDAO().getAddresses());
    }

}