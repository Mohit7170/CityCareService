package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.modals.ServiceModal
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.app.citycareservice.R
import android.widget.TextView
import android.widget.LinearLayout
import com.app.citycareservice.databinding.ItemContainerDiffServicesBinding
import com.app.citycareservice.databinding.ItemContainerServicesBinding
import com.app.citycareservice.modals.allService.Result
import com.app.citycareservice.modals.allService.Service
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import java.util.ArrayList

class FirstServicesAdapter(
    private val activity: Activity,
    private val allServices: List<Service>
) : RecyclerView.Adapter<FirstServicesAdapter.ViewHolder>(), Params {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FirstServicesAdapter.ViewHolder {
        val binding = ItemContainerServicesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {

            val service = allServices[position]
            serviceNameTv.text = service.title
            HelperClass.setImage(activity, service.icon, serviceIv, R.drawable.circle)
        }

       /* holder.main_layout_single_service.setOnClickListener {
            //                activity.startActivity(new Intent(activity, ServicesActivity.class).putExtra(INTENT_KEY_SERVICE_CATEGORY_ID, ""));
        }*/
    }

    override fun getItemCount(): Int {
        return allServices.size
    }

    inner class ViewHolder(val binding: ItemContainerServicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "FirstServicesAdapter"
    }
}