package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.app.citycareservice.databinding.ItemContainerMyBookingsBinding
import com.app.citycareservice.modals.order.myOrders.Result
import com.app.citycareservice.utils.Params

class BookingHistoryAdapter(
    private val activity: Activity,
    private val bookingHistoryResponses: List<Result>,
) :
    RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder>(), Params {

    /*  fun setData(bookingHistoryResponses: List<Result>) {
          Log.d(TAG, "setData: ")
          val oldSize = this.bookingHistoryResponses.size
          this.bookingHistoryResponses.clear()
          this.bookingHistoryResponses.addAll(bookingHistoryResponses)
          notifyItemRangeChanged(0, oldSize)
      }

      fun addData(bookingHistoryResponses: List<Result>) {
          val oldSize = this.bookingHistoryResponses.size
          this.bookingHistoryResponses.addAll(bookingHistoryResponses)
          notifyItemRangeInserted(oldSize + 1, this.bookingHistoryResponses.size)
      }
  */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContainerMyBookingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
//            val booking = bookingHistoryResponses[position]
            val booking = bookingHistoryResponses[0]
            Log.d(TAG, "onBindViewHolder: ")
//TODO Api Pending
            serviceDateTv.text = booking.service_date
            serviceTimeTv.text = booking.service_time
            reviewTv.text = booking.remarks
            servicePriceTv.text ="₹ ${booking.price}"
//            ratingBar.rating = booking.rating.toFloat()

        }

    }

    override fun getItemCount(): Int {
//        return bookingHistoryResponses.size
        return bookingHistoryResponses.size
    }

    inner class ViewHolder(val binding: ItemContainerMyBookingsBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "BookingHistoryAdapter"
    }
}