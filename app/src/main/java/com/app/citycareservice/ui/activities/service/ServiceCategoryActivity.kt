package com.app.citycareservice.ui.activities.service

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import com.app.citycareservice.adapters.recycler_view.FirstServicesAdapter
import android.os.Bundle
import com.app.citycareservice.databinding.ActivityServiceCategoryBinding

class ServiceCategoryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityServiceCategoryBinding

    private lateinit var activity: Activity
    private lateinit var firstServicesAdapter: FirstServicesAdapter
    private lateinit var serviceCategoryId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        firstServicesAdapter = FirstServicesAdapter(activity)
//        binding.servicesRv.adapter = firstServicesAdapter

//            firstServicesAdapter = new FirstServicesAdapter(activity);
//        binding.relatedServicesRv.adapter = firstServicesAdapter

        try {
            serviceCategoryId = intent.getStringExtra("serviceCategoryId")
                ?: throw Exception("Service Category Id can't be null")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.backBtn.setOnClickListener { onBackPressed() }
    }

    companion object {
        private const val TAG = "ServiceCategoryActivity"
    }
}