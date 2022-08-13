package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.modals.historyModal.BookingHistoryResponse
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.app.citycareservice.R
import com.google.android.material.imageview.ShapeableImageView
import android.widget.TextView
import android.widget.RatingBar
import com.app.citycareservice.modals.order.myOrders.Result
import com.app.citycareservice.utils.Params
import java.util.ArrayList

class BookingHistoryAdapter(private val activity: Activity) : RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder>(), Params {
    private val bookingHistoryResponses: MutableList<Result> = ArrayList()
    fun setData(bookingHistoryResponses: List<Result>?) {
        val oldSize = this.bookingHistoryResponses.size
        this.bookingHistoryResponses.clear()
        this.bookingHistoryResponses.addAll(bookingHistoryResponses!!)
        notifyItemRangeChanged(0, oldSize)
    }

    fun addData(bookingHistoryResponses: List<Result>) {
        val oldSize = this.bookingHistoryResponses.size
        this.bookingHistoryResponses.addAll(bookingHistoryResponses)
        notifyItemRangeInserted(oldSize + 1, this.bookingHistoryResponses.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_my_bookings, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingHistoryResponse = bookingHistoryResponses[position]
    }

    override fun getItemCount(): Int {
        return bookingHistoryResponses.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val user_iv: ShapeableImageView
        private val user_name: TextView
        private val service_name_tv: TextView
        private val service_date_tv: TextView
        private val service_time_tv: TextView
        private val rating_bar: RatingBar
        private val service_price_tv: TextView
        private val review_tv: TextView

        init {
            user_iv = itemView.findViewById(R.id.user_iv)
            user_name = itemView.findViewById(R.id.user_name)
            service_name_tv = itemView.findViewById(R.id.service_name_tv)
            service_date_tv = itemView.findViewById(R.id.service_date_tv)
            service_time_tv = itemView.findViewById(R.id.service_time_tv)
            rating_bar = itemView.findViewById(R.id.rating_bar)
            service_price_tv = itemView.findViewById(R.id.service_price_tv)
            review_tv = itemView.findViewById(R.id.review_tv)
        }
    }
}