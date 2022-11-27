package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ItemContainerReviewsBinding
import com.app.citycareservice.modals.serviceCategory.Service
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params

class ReviewAdapter(private val activity: Activity, private val services: List<Service>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>(), Params {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemContainerReviewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.binding) {

            val service = services[position]

            HelperClass.setImage(
                activity,
                service.image,
                userIv,
                R.drawable.ic_placeholder
            )

            serviceNameTv.text = service.title

//            root.setOnClickListener { activity.startActivity(Intent(activity,)) }


        }

    }

    override fun getItemCount(): Int {
        return services.size
    }

    inner class ViewHolder(val binding: ItemContainerReviewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "ServicesDetailAdapter"
    }
}