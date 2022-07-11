package com.app.citycareservice.dialogs.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.app.citycareservice.utils.roomDB.AddressDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class BookingBottomSheet extends BottomSheetDialog implements Params {

    private static final String TAG = "AddAddressBottomSheet";
    private Activity activity;

    private String id = DEFAULT_EMPTY_STRING;

    private TextView place_name_tv;
    private TextView address_tv;
    private TextInputLayout house_no_tif;
    private TextInputLayout landmark_tif;
    private TextInputLayout name_tif;
    private MaterialButton save_btn;

    private SharedPrefHandler prefHandler;

    public BookingBottomSheet(@NonNull Context context) {
        super(context);
        setContentView(R.layout.bottom_sheet_add_address);

        activity = (Activity) context;
        init();
        show();
    }

    public BookingBottomSheet(@NonNull Context context, String id) {
        super(context);
        setContentView(R.layout.bottom_sheet_add_address);

        activity = (Activity) context;
        id = this.id;
        init();
        show();
    }

    private void init() {

        place_name_tv = findViewById(R.id.place_name_tv);
        address_tv = findViewById(R.id.address_tv);
        house_no_tif = findViewById(R.id.house_no_tif);
        landmark_tif = findViewById(R.id.landmark_tif);
        name_tif = findViewById(R.id.name_tif);
        save_btn = findViewById(R.id.save_btn);

        prefHandler = new SharedPrefHandler(activity);

        String address = prefHandler.getString(SP_KEY_TEMP_USER_LOCALITY);
        String city = prefHandler.getString(SP_KEY_TEMP_USER_SUB_LOCALITY);

        place_name_tv.setText(city);
        address_tv.setText(address);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void validateForm() {

        house_no_tif.setErrorEnabled(false);
        landmark_tif.setErrorEnabled(false);
        name_tif.setErrorEnabled(false);

        String place = place_name_tv.getText().toString().trim();
        String address = address_tv.getText().toString().trim();
        String house_no = Objects.requireNonNull(house_no_tif.getEditText()).getText().toString().trim();
        String landmark = Objects.requireNonNull(landmark_tif.getEditText()).getText().toString().trim();
        String name = Objects.requireNonNull(name_tif.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(place) || TextUtils.isEmpty(address)) {
            HelperClass.showToast(activity, "Please Update Location");
            return;
        }
        if (TextUtils.isEmpty(house_no)) {
            house_no_tif.setError("House Number cannot be empty");
            house_no_tif.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            name_tif.setError("Name cannot be empty");
            name_tif.setFocusable(true);
            return;
        }

        addAddressApi(house_no, place, address, landmark, name);

    }

    private void addAddressApi(String house_no, String place, String address, String landmark, String name) {
        //Hit Address API


        AddressModal addressModal = new AddressModal();
        addressModal.setHouseNo(house_no);
        addressModal.setName(name);
        addressModal.setLandmark(landmark);
        addressModal.setPlace(place);
        addressModal.setAddress(address);

        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
 /*       if (!TextUtils.isEmpty(id) || !TextUtils.equals(DEFAULT_EMPTY_STRING, id) || addressDatabase.addressDAO().addressExists("id", id))
            addressDatabase.addressDAO().updateAddress(addressModal);
        else*/
        addressDatabase.addressDAO().insertAddress(addressModal);

        HelperClass.showToast(activity, "Address Added Successfully");
        dismiss();
        cancel();
    }

}
