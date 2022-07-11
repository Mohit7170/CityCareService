package com.app.citycareservice.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.interfaces.click.DateSelect;
import com.app.citycareservice.utils.Params;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.ViewHolder> implements Params {

    private static final String TAG = "SelectDateAdapter";
    private final Activity activity;
    private final DateSelect dateSelect;

    private int selected_position = 0; // You have to set this globally in the Adapter class
    private boolean isClicked = false;

    public SelectDateAdapter(Activity activity, DateSelect dateSelect) {
        this.activity = activity;
        this.dateSelect = dateSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_service_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EE");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, position);
        holder.day_tv.setText(sdf1.format(calendar.getTime()));
        holder.date_tv.setText(sdf.format(calendar.getTime()));
        holder.date = calendar.getTime().toString();

        int color = selected_position == holder.getAdapterPosition() ? R.color.purple_200_40 : R.color.light_grey;
        holder.main_layout_single_date.setCardBackgroundColor(ContextCompat.getColor(activity, color));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, categoriesModal.getCategoryName() + " Clicked", Toast.LENGTH_SHORT).show();

                // Below line is just like a safety check, because sometimes holder could be null,
                // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
                if (holder.getAdapterPosition() == RecyclerView.NO_POSITION || selected_position == holder.getAdapterPosition())
                    return;

                // Updating old as well as new positions

                notifyItemChanged(selected_position);
                selected_position = holder.getAdapterPosition();
                notifyItemChanged(selected_position);

                // Do your another stuff for your onClick

                dateSelect.onClick(SelectDateAdapter.class.getSimpleName(), holder.date);
            }
        });
//
//        if (!isClicked && position == 2) {
//            holder.itemView.performClick();
//            isClicked = true;
//        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView main_layout_single_date;
        private final TextView day_tv;
        private final TextView date_tv;
        private String date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_layout_single_date = itemView.findViewById(R.id.main_layout_single_date);
            day_tv = itemView.findViewById(R.id.day_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
        }
    }
}
