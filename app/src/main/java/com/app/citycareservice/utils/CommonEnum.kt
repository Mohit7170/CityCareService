package com.app.citycareservice.utils

class CommonEnum {

    enum class Status(val value:String){
        PendingPayment("pending_payment"),
        Accept("accept"),
        Failed("failed"),
        Pending("pending"),
        Processing("processing"),
        OnHold("on_hold"),
        Canceled("canceled"),
        Refunded("refunded"),
        AuthenticationRequired("authentication_required"),
        CheckoutDraft("checkout_draft"),
    }

}