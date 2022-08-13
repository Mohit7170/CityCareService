package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.ui.activities.service.ServiceCategoryActivity
import com.app.citycareservice.databinding.ItemContainerDiffServicesBinding
import com.app.citycareservice.modals.allService.Result
import com.app.citycareservice.utils.Params

class ServicesAdapter(
    private val activity: Activity,
    private val allServices: List<Result>
) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>(), Params {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServicesAdapter.ViewHolder {
        val binding = ItemContainerDiffServicesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.binding) {
            val service = allServices[position]
            serviceHeadingTv.text = service.name
            servicesRv.adapter = FirstServicesAdapter(activity, service.services)
            serviceHeadingTv.setOnClickListener {
                activity.startActivity(
                    Intent(activity, ServiceCategoryActivity::class.java).putExtra(
                        Params.INTENT_KEY_SERVICE_CATEGORY_ID, service._id
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return allServices.size
    }

    inner class ViewHolder(val binding: ItemContainerDiffServicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "ServicesAdapter"
    }
}