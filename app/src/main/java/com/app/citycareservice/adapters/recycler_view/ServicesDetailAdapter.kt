package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ItemContainerServiceDetailBinding
import com.app.citycareservice.modals.serviceCategory.Service
import com.app.citycareservice.ui.activities.service.ServiceDetailActivity
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params

class ServicesDetailAdapter(private val activity: Activity, private val services: List<Service>) :
    RecyclerView.Adapter<ServicesDetailAdapter.ViewHolder>(), Params {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemContainerServiceDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.binding) {
            try {

                val service = services[position]

                HelperClass.setImage(
                    activity,
                    service.image,
                    serviceIv,
                    R.drawable.ic_placeholder
                )

                serviceNameTv.text = service.title
                servicePriceTv.text = "₹ ${service.price}"
                serviceTimeTv.text = service.completion_time

              /*  bookNowTv.setOnClickListener {
                    activity.startActivity(
                        Intent(
                            activity,
                            BookServicesActivity::class.java
                        ).putExtra(
                            Params.INTENT_KEY_SERVICE_ID,
                            services[holder.adapterPosition]._id
                        )
                    )
                }
*/
                root.setOnClickListener {
                    activity.startActivity(
                        Intent(activity, ServiceDetailActivity::class.java).putExtra(
                            Params.INTENT_KEY_SERVICE_ID, services[holder.adapterPosition]._id
                        )
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun getItemCount(): Int {
        return services.size
    }

    inner class ViewHolder(val binding: ItemContainerServiceDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "ServicesDetailAdapter"
    }
}