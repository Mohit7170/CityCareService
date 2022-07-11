package com.app.citycareservice.modals;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class ServiceCategoryModal extends ViewModel {

    private String service_cat_name;
    private String service_cat_id;
    private String service_cat_image;
    private List<ServiceModal> serviceModal;

    public List<ServiceModal> getServiceModal() {
        return serviceModal;
    }

    public void setServiceModal(List<ServiceModal> serviceModal) {
        this.serviceModal = serviceModal;
    }

    public String getService_cat_name() {
        return service_cat_name;
    }

    public void setService_cat_name(String service_cat_name) {
        this.service_cat_name = service_cat_name;
    }

    public String getService_cat_id() {
        return service_cat_id;
    }

    public void setService_cat_id(String service_cat_id) {
        this.service_cat_id = service_cat_id;
    }

    public String getService_cat_image() {
        return service_cat_image;
    }

    public void setService_cat_image(String service_cat_image) {
        this.service_cat_image = service_cat_image;
    }
}
