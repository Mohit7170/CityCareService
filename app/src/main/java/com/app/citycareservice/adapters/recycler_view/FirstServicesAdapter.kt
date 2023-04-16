package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ItemContainerServicesBinding
import com.app.citycareservice.modals.allService.Service
import com.app.citycareservice.ui.activities.service.ServiceDetailActivity
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params

class FirstServicesAdapter(
    private val activity: Activity,
    private val allService: List<Service>
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

            val service = allService[position]
            serviceNameTv.text = service.title
            HelperClass.setImage(activity, service.icon, serviceIv, R.drawable.ic_placeholder)

            root.setOnClickListener {
                activity.startActivity(
                    Intent(activity, ServiceDetailActivity::class.java).putExtra(
                        Params.INTENT_KEY_SERVICE_ID, allService[holder.adapterPosition]._id
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return allService.size
    }

    inner class ViewHolder(val binding: ItemContainerServicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "FirstServicesAdapter"
    }
}