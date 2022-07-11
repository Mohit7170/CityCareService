package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.ServiceModal;
import com.app.citycareservice.utils.Params;

import java.util.ArrayList;
import java.util.List;

public class FirstServicesAdapter extends RecyclerView.Adapter<FirstServicesAdapter.ViewHolder> implements Params {

    private final Activity activity;
    private final List<ServiceModal> serviceModals = new ArrayList<>();

    public FirstServicesAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(List<ServiceModal> serviceModals) {
        int oldSize = this.serviceModals.size();
        this.serviceModals.clear();
        this.serviceModals.addAll(serviceModals);
        notifyItemRangeChanged(0, oldSize);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_services, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.main_layout_single_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.startActivity(new Intent(activity, ServicesActivity.class).putExtra(INTENT_KEY_SERVICE_CATEGORY_ID, ""));
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceModals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView service_name_tv;
        private final ImageView service_iv;
        private final LinearLayout main_layout_single_service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            service_name_tv = itemView.findViewById(R.id.service_name_tv);
            service_iv = itemView.findViewById(R.id.service_iv);
            main_layout_single_service = itemView.findViewById(R.id.main_layout_single_service);
        }
    }
}
