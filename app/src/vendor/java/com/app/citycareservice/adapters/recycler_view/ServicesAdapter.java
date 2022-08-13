package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.ui.activities.ServiceCategoryActivity;
import com.app.citycareservice.modals.ServiceCategoryModal;
import com.app.citycareservice.utils.Params;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> implements Params {

    private final Activity activity;
    private final ArrayList<ServiceCategoryModal> serviceCategoryModals = new ArrayList<>();

    public ServicesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_diff_services, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceCategoryModal serviceModal = serviceCategoryModals.get(position);

        holder.service_heading_tv.setText(serviceModal.getService_cat_name());

        FirstServicesAdapter adapter = new FirstServicesAdapter(activity);
        holder.services_rv.setAdapter(adapter);
        adapter.setData(serviceModal.getServiceModal());

        holder.service_heading_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ServiceCategoryActivity.class).
                        putExtra(INTENT_KEY_SERVICE_CATEGORY_ID, serviceModal.getService_cat_id()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceCategoryModals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView service_heading_tv;
        private final TextView view_more_tv;
        private final RecyclerView services_rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            service_heading_tv = itemView.findViewById(R.id.service_heading_tv);
            view_more_tv = itemView.findViewById(R.id.view_more_tv);
            services_rv = itemView.findViewById(R.id.services_rv);
        }
    }
}
