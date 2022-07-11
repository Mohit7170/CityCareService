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
import com.app.citycareservice.interfaces.click.TimeSelect;
import com.app.citycareservice.utils.Params;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.ViewHolder> implements Params {

    private static final String TAG = "SelectTimeAdapter";
    private final Activity activity;
    private final Date date;
    private final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    private final Calendar calendar = Calendar.getInstance();
    private final TimeSelect timeSelect;

    private int selected_position = 0;
    private boolean isClicked = false;

    public SelectTimeAdapter(Activity activity, TimeSelect timeSelect) throws ParseException {
        this.activity = activity;
        this.timeSelect = timeSelect;
        date = sdf.parse("08:00 AM");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_service_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, position * 30);
        holder.time_tv.setText(sdf.format(calendar.getTime()));

        int color = selected_position == holder.getAdapterPosition() ? R.color.purple_200_40 : R.color.light_grey;
        holder.main_layout_single_time.setCardBackgroundColor(ContextCompat.getColor(activity, color));

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

                timeSelect.onClick(SelectTimeAdapter.class.getSimpleName(), holder.time_tv.getText().toString());
            }
        });

//        if (!isClicked && position == 2) {
//            holder.itemView.performClick();
//            isClicked = true;
//        }

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView main_layout_single_time;
        private final TextView time_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_layout_single_time = itemView.findViewById(R.id.main_layout_single_time);
            time_tv = itemView.findViewById(R.id.time_tv);
        }
    }
}
