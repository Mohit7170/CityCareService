package com.app.citycareservice.utils

import androidx.annotation.ColorRes
import com.app.citycareservice.R

class CommonEnum {

    enum class Status(val value:String,@ColorRes val color:Int){
        pending_payment("pending_payment",color = R.color.red),
        completed("completed",color = R.color.call_green),
        accept("accept",color = R.color.rating_color),
        failed("failed",color = R.color.red),
        pending("pending",color = R.color.red),
        processing("processing",color = R.color.red),
        on_hold("on_hold",color = R.color.red),
        canceled("canceled",color = R.color.red),
        refunded("refunded",color = R.color.red),
        authentication_required("authentication_required",color = R.color.red),
        checkout_draft("checkout_draft",color = R.color.red),
    }

}