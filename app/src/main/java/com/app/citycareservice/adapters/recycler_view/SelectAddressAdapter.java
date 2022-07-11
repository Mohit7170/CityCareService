package com.app.citycareservice.adapters.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.interfaces.click.AddressSelect;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.utils.Params;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.ViewHolder> implements Params {

    private final List<AddressModal> addressModals = new ArrayList<>();

    private final AddressSelect addressSelect;

    public SelectAddressAdapter(AddressSelect addressSelect) {
        this.addressSelect = addressSelect;
    }

    public void setData(List<AddressModal> addressModal) {
        int oldSIze = this.addressModals.size();
        this.addressModals.clear();
        this.addressModals.addAll(addressModal);
        notifyItemRangeChanged(0, oldSIze);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_select_addresses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AddressModal addressModal = addressModals.get(position);

        holder.address_type_tv.setText(addressModal.getName());

        String fullAddress = addressModal.getName().concat(" , ")
                .concat(addressModal.getHouseNo()).concat(" , ")
                .concat(addressModal.getLandmark()).concat(" , ")
                .concat(addressModal.getAddress());

        holder.address_tv.setText(fullAddress);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressSelect.onClick(addressModal);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressModals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView address_type_tv;
        private final TextView address_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address_type_tv = itemView.findViewById(R.id.address_type_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
        }
    }
}
