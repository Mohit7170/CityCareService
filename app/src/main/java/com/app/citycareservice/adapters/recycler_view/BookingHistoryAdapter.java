package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.historyModal.BookingHistoryResponse;
import com.app.citycareservice.utils.Params;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> implements Params {

    private final Activity activity;
    private final List<BookingHistoryResponse> bookingHistoryResponses = new ArrayList<>();

    public BookingHistoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(List<BookingHistoryResponse> bookingHistoryResponses) {
        int oldSize = this.bookingHistoryResponses.size();
        this.bookingHistoryResponses.clear();
        this.bookingHistoryResponses.addAll(bookingHistoryResponses);
        notifyItemRangeChanged(0, oldSize);
    }

    public void addData(@NonNull List<BookingHistoryResponse> bookingHistoryResponses) {
        int oldSize = this.bookingHistoryResponses.size();
        this.bookingHistoryResponses.addAll(bookingHistoryResponses);
        notifyItemRangeInserted(oldSize + 1, this.bookingHistoryResponses.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_my_bookings, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingHistoryResponse bookingHistoryResponse = bookingHistoryResponses.get(position);

    }

    @Override
    public int getItemCount() {
        return bookingHistoryResponses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ShapeableImageView user_iv;
        private final TextView user_name;
        private final TextView service_name_tv;
        private final TextView service_date_tv;
        private final TextView service_time_tv;
        private final RatingBar rating_bar;
        private final TextView service_price_tv;
        private final TextView review_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_iv = itemView.findViewById(R.id.user_iv);
            user_name = itemView.findViewById(R.id.user_name);
            service_name_tv = itemView.findViewById(R.id.service_name_tv);
            service_date_tv = itemView.findViewById(R.id.service_date_tv);
            service_time_tv = itemView.findViewById(R.id.service_time_tv);
            rating_bar = itemView.findViewById(R.id.rating_bar);
            service_price_tv = itemView.findViewById(R.id.service_price_tv);
            review_tv = itemView.findViewById(R.id.review_tv);

        }
    }
}
