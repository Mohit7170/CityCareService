package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.ServiceCategoryModal;
import com.app.citycareservice.utils.Params;

import java.util.ArrayList;

public class ServicesDetailAdapter extends RecyclerView.Adapter<ServicesDetailAdapter.ViewHolder> implements Params {

    private final Activity activity;
    private final ArrayList<ServiceCategoryModal> serviceCategoryModals = new ArrayList<>();

    public ServicesDetailAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_service_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceCategoryModal serviceModal = serviceCategoryModals.get(position);

        holder.book_now_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceCategoryModals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView service_name_tv;
        private final TextView service_price_tv;
        private final TextView service_time_tv;
        private final TextView offer_tv;
        private final TextView book_now_tv;
        private final TextView service_iv;
        private final RecyclerView offers_rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            service_name_tv = itemView.findViewById(R.id.service_name_tv);
            service_price_tv = itemView.findViewById(R.id.service_price_tv);
            service_time_tv = itemView.findViewById(R.id.service_time_tv);
            offer_tv = itemView.findViewById(R.id.offer_tv);
            book_now_tv = itemView.findViewById(R.id.book_now_tv);
            service_iv = itemView.findViewById(R.id.service_iv);
            offers_rv = itemView.findViewById(R.id.offers_rv);

        }
    }
}
