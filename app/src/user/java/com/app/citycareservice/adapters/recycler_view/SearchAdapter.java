package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.search.service.Result;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Params {

    private final Activity activity;
    private final List<Result> results = new ArrayList<>();

    public SearchAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(List<Result> results) {
        int old_size = this.results.size();
        this.results.clear();
        this.results.addAll(results);
        notifyItemRangeChanged(0, old_size);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Result serviceModal = results.get(position);


        HelperClass.setImage(activity, serviceModal.getImage(), holder.service_iv, R.drawable.circle);
        holder.service_name_tv.setText(serviceModal.getTitle());
        holder.service_desc_tv.setText(serviceModal.getDescription());

        holder.main_layout_single_service_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.showToast(activity, activity.getString(R.string.under_development));
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView service_name_tv;
        private final TextView service_desc_tv;
        private final ShapeableImageView service_iv;
        private final ConstraintLayout main_layout_single_service_search;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            service_name_tv = itemView.findViewById(R.id.service_name_tv);
            service_desc_tv = itemView.findViewById(R.id.service_desc_tv);
            service_iv = itemView.findViewById(R.id.service_iv);
            main_layout_single_service_search = itemView.findViewById(R.id.main_layout_single_service_search);
        }
    }
}
