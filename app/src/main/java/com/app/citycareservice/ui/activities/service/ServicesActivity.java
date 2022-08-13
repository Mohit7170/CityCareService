package com.app.citycareservice.ui.activities.service;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;

public class ServicesActivity extends AppCompatActivity {

    private Activity activity;
    private RecyclerView all_services_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serives);

        activity = ServicesActivity.this;
        all_services_rv = findViewById(R.id.all_services_rv);

       /* ArrayList<ServiceModal> serviceModals = new ArrayList<>();
        all_services_rv.setAdapter(new AllServicesAdapter(activity, serviceModals));*/
    }
}