package com.app.citycareservice.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.adapters.recycler_view.FirstServicesAdapter;
import com.google.android.material.imageview.ShapeableImageView;

public class ServiceCategoryActivity extends AppCompatActivity {

    private static final String TAG = "ServiceCategoryActivity";
    private Activity activity = ServiceCategoryActivity.this;

    private ShapeableImageView back_iv;
    private ImageView service_iv;
    private TextView service_name_tv;
    private TextView rating_tv;
    private TextView detail_tv;
    private TextView related_services;
    private RecyclerView related_services_rv;
    private RecyclerView service_rv;

    private String service_category_id;
    private FirstServicesAdapter firstServicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category);

        back_iv = findViewById(R.id.back_iv);
        service_iv = findViewById(R.id.service_iv);
        service_name_tv = findViewById(R.id.service_name_tv);
        rating_tv = findViewById(R.id.rating_tv);
        detail_tv = findViewById(R.id.detail_tv);
        related_services = findViewById(R.id.related_services);
        related_services_rv = findViewById(R.id.related_services_rv);
        service_rv = findViewById(R.id.services_rv);

        firstServicesAdapter = new FirstServicesAdapter(activity);
        service_rv.setAdapter(firstServicesAdapter);

            firstServicesAdapter = new FirstServicesAdapter(activity);
        related_services_rv.setAdapter(firstServicesAdapter);

        Intent intent = getIntent();
        service_category_id = intent.getStringExtra("serviceCategoryId");

        getDetail();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });

    }

    private void getDetail() {
//        Hit Api

//        firstServicesAdapter.setData();

    }
}
