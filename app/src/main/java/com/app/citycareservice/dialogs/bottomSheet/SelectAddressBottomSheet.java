package com.app.citycareservice.dialogs.bottomSheet;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.activities.AddAddressActivity;
import com.app.citycareservice.adapters.recycler_view.SelectAddressAdapter;
import com.app.citycareservice.interfaces.click.AddressSelect;
import com.app.citycareservice.interfaces.sheetDismissListner;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.app.citycareservice.utils.roomDB.AddressDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SelectAddressBottomSheet extends BottomSheetDialog implements Params, AddressSelect, sheetDismissListner {

    private static final String TAG = "AddAddressActivity";
    private final AppCompatActivity activity;

    private ImageView add_address_iv;
    private RecyclerView addresses_rv;

    private final AddressSelect addressSelect;

    private AddAddressActivity addAddressActivity;
    private SelectAddressAdapter selectAddressAdapter;
    private AddressDatabase addressDatabase;

    public SelectAddressBottomSheet(@NonNull Context context, AddressSelect addressSelect) {
        super(context);
        setContentView(R.layout.bottom_sheet_select_address);

        activity = (AppCompatActivity) context;
        this.addressSelect = addressSelect;
        init();
        show();
    }

    private void init() {

        addresses_rv = findViewById(R.id.addresses_rv);
        add_address_iv = findViewById(R.id.add_address_iv);

        selectAddressAdapter = new SelectAddressAdapter(this);
        addresses_rv.setAdapter(selectAddressAdapter);
        addAddressActivity = new AddAddressActivity(this);

        addressDatabase = AddressDatabase.getInstance(activity);
        setData();

        add_address_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, AddAddressActivity.class));
                dismiss();
//                addAddressActivity.show(activity.getSupportFragmentManager(), "ppp");
            }
        });

//        addAddressActivity.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                selectAddressAdapter.setData(addressDatabase.addressDAO().getAddresses());
//            }
//        });

       /* select_address_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
*/
    }


    @Override
    public void onClick(AddressModal addressModal) {
        SharedPrefHandler prefHandler = new SharedPrefHandler(activity);
        prefHandler.setIntValue(SP_KEY_LAST_USED_ADDRESS_ID, addressModal.getId());
        addressSelect.onClick(addressModal);
        dismiss();
    }

    @Override
    public void setData() {
        selectAddressAdapter.setData(addressDatabase.addressDAO().getAddresses());
    }
}
