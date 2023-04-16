package com.app.citycareservice.adapters.recycler_view

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ItemContainerMyBookingsBinding
import com.app.citycareservice.modals.order.myOrders.Result
import com.app.citycareservice.utils.CommonEnum
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params

class BookingHistoryAdapter(
    private val activity: Activity,
    private val bookingHistoryResponses: List<Result>,
) : RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder>(), Params {

    /*  fun setData(bookingHistoryResponses: List<Result>) {service_price_tv
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
            val booking = bookingHistoryResponses[position]
            val service = booking.services[0]

            val status = booking.status

            orderStatus.text = "${status.value.capitalize()}"
            orderStatus.setTextColor(activity.resources.getColor(status.color))

            if (status != CommonEnum.Status.completed) {
                servicePriceTv.text = "₹ ${booking.services[0].price}"
                userIv.setImageDrawable(activity.getDrawable(R.drawable.progress))
                review.visibility = View.GONE
                reviewBar.visibility = View.GONE
            } else {
                servicePriceTv.text = "₹ ${booking.price}"
                val user = booking.user[0]
                HelperClass.setImage(activity, user.profile_pic, userIv, R.drawable.ic_placeholder)
                userName.visibility = View.VISIBLE
                userName.text = user.name

                booking.review?.let {
                    review.visibility = View.VISIBLE
                    reviewTv.visibility = View.VISIBLE
                    reviewTv.text = "${it.review} \t(${it.rate})"
                }

                val rating = service.rating
                if (!TextUtils.isEmpty(rating) && !TextUtils.equals(rating, "0.0")) {
                    reviewBar.visibility = View.VISIBLE
                    reviewBar.rating = rating.toFloat()
                }
            }

            serviceNameTv.text = service.title
            serviceDateTv.text = booking.service_date
            serviceTimeTv.text = booking.service_time

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