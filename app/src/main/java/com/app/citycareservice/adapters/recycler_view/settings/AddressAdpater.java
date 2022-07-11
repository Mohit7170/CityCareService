package com.app.citycareservice.adapters.recycler_view.settings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.roomDB.AddressDatabase;

import java.util.ArrayList;
import java.util.List;


public class AddressAdpater extends RecyclerView.Adapter<AddressAdpater.ViewHolder> implements Params {

    private final Activity activity;
    private List<AddressModal> addressModals = new ArrayList<>();

    public AddressAdpater(Activity activity) {
        this.activity = activity;
    }

    public AddressAdpater(Activity activity, List<AddressModal> addressModals) {
        this.activity = activity;
        this.addressModals = addressModals;
    }

    public void setData(List<AddressModal> addressModals) {
        int oldSize = this.addressModals.size();
        this.addressModals.clear();
        this.addressModals.addAll(addressModals);
        notifyItemRangeChanged(oldSize, addressModals.size());
    }

 /*   public void addData(AddressModal addressModal) {
        int oldSize = this.addressModals.size();
        this.addressModals.clear();
        this.addressModals.addAll(addressModals);
        notifyItemRangeInserted(oldSize, addressModals.size());
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_addresses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AddressModal addressModal = addressModals.get(position);

        String fullAddress = addressModal.getName().concat(" , ")
                .concat(addressModal.getHouseNo()).concat(" , ")
                .concat(addressModal.getLandmark()).concat(" , ")
                .concat(addressModal.getAddress());

        holder.address_tv.setText(fullAddress);

        holder.options_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddress(holder.getAdapterPosition());
                //                openOptionsPopup(v, addressModal.getId());
            }
        });
    }

    private void deleteAddress(int position) {
        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
        addressDatabase.addressDAO().deleteAddress(addressModals.get(position));
        addressModals.remove(position);
        notifyItemRemoved(position);
    }

    private void openOptionsPopup(View v, int id) {

        PopupMenu popupMenu = new PopupMenu(activity, v);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.popup_address_option_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int item_id = menuItem.getItemId();

//                if (item_id == )

                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();

    }

    @Override
    public int getItemCount() {
        return addressModals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView address_tv;
        private final ImageView options_iv;
//        private final ImageView service_iv;
//        private final LinearLayout main_layout_single_service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address_tv = itemView.findViewById(R.id.address_tv);
            options_iv = itemView.findViewById(R.id.options_iv);
//            service_iv = itemView.findViewById(R.id.service_iv);
//            main_layout_single_service = itemView.findViewById(R.id.main_layout_single_service);
        }
    }
}
