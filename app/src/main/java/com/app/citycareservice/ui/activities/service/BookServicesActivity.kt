package com.app.citycareservice.ui.activities.service

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.citycareservice.R
import com.app.citycareservice.adapters.recycler_view.SelectDateAdapter
import com.app.citycareservice.adapters.recycler_view.SelectTimeAdapter
import com.app.citycareservice.databinding.ActivityBookServiceBinding
import com.app.citycareservice.interfaces.click.AddressSelect
import com.app.citycareservice.interfaces.click.DateSelect
import com.app.citycareservice.interfaces.click.TimeSelect
import com.app.citycareservice.interfaces.order.Service
import com.app.citycareservice.modals.AddressModal.AddressModal
import com.app.citycareservice.modals.order.CreateOrderResponse
import com.app.citycareservice.ui.dialogs.bottomSheet.AddAddressBottomSheet
import com.app.citycareservice.ui.dialogs.bottomSheet.SelectAddressBottomSheet
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.Params.INTENT_KEY_SERVICE_ID
import com.app.citycareservice.utils.SharedPrefHandler
import com.app.citycareservice.utils.roomDB.AddressDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.ParseException

class BookServicesActivity : AppCompatActivity(), Params, AddressSelect, DateSelect, TimeSelect {

    private lateinit var activity: Activity
    private lateinit var binding: ActivityBookServiceBinding
    private lateinit var prefHandler: SharedPrefHandler

    private lateinit var addressModal: AddressModal
    private lateinit var selectDateAdapter: SelectDateAdapter
    private lateinit var selectTimeAdapter: SelectTimeAdapter

    private var selectAddress: String = ""
    private var timeToFinish: String = ""
    private var selectDate: String = ""
    private var selectTime: String = ""
    private var serviceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this@BookServicesActivity
        prefHandler = SharedPrefHandler(activity)
        selectDateAdapter = SelectDateAdapter(activity, this)

        try {
            selectTimeAdapter = SelectTimeAdapter(activity, this)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val addressDatabase = AddressDatabase.getInstance(activity).addressDAO()

        if (addressDatabase.addresses.isNullOrEmpty()) {
            val addAddressBottomSheet = AddAddressBottomSheet {
                addressModal =
                    addressDatabase.addresses[0]
                onClick(addressModal)
            }
            addAddressBottomSheet.show(supportFragmentManager, "AddAddressBottomFrag")
        } else {
            addressModal =
                addressDatabase.addresses[0]

            onClick(addressModal)
        }

        with(binding) {
            changeAddressTv.setOnClickListener(View.OnClickListener {
                SelectAddressBottomSheet(
                    activity as BookServicesActivity, this@BookServicesActivity
                )
            })

            val intent = intent
            try {

                serviceId = intent.getStringExtra(INTENT_KEY_SERVICE_ID)
                    ?: throw Exception("Service Id can't be null")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            timeToFinish = "45"
            HelperClass.showLoader(activity)
            serviceTimeTv.text = "Your Service take aproxx. $timeToFinish mins."
            dateRv.adapter = selectDateAdapter
            timeRv.adapter = selectTimeAdapter
            Handler().postDelayed({ //                TODO change this way
                dateRv.findViewHolderForAdapterPosition(2)?.itemView?.performClick()
                timeRv.findViewHolderForAdapterPosition(4)?.itemView?.performClick()
                HelperClass.hideLoader()
            }, 1000)
            bookBtn.setOnClickListener{ validateForm() }

        }

    }

    private fun validateForm() {
        if (TextUtils.isEmpty(selectAddress)) {
            HelperClass.showToast(activity, "Please Select Address")
            return
        }
        if (TextUtils.isEmpty(selectDate)) {
            HelperClass.showToast(activity, "Please Select Date")
            return
        }
        if (TextUtils.isEmpty(selectTime)) {
            HelperClass.showToast(activity, "Please Select Time")
            return
        }
        bookServiceAPi()
    }

    /*TODO Check Api*/
    private fun bookServiceAPi() {
        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity)
            val prefHandler = SharedPrefHandler(activity)
            val api = ApiClient.apiService(activity).create(Service::class.java)
            val call = api.createOrder(
                authToken = prefHandler.getString(Params.SP_KEY_AUTH_TOKEN),
                userId = prefHandler.getString(
                    Params.SP_KEY_USER_ID
                ),
                serviceId = serviceId,
                serviceDate = selectDate,
                serviceTime = selectTime,
                remarks = Params.DEFAULT_EMPTY_STRING,
                address = selectAddress
            )
            call.enqueue(object : Callback<CreateOrderResponse?> {
                override fun onResponse(
                    call: Call<CreateOrderResponse?>, response: Response<CreateOrderResponse?>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true)
                        return
                    }
                    val apiResponse = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {
                        HelperClass.showToast(activity, apiResponse.message)
                        setResult(RESULT_OK)
                        finish()
                    } else HelperClass.showToast(
                        activity, activity.getString(R.string.something_went_wrong)
                    )
                    HelperClass.hideLoader()
                }

                override fun onFailure(call: Call<CreateOrderResponse?>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    HelperClass.hideLoader()
                    HelperClass.showToast(
                        activity, activity.getString(R.string.something_went_wrong)
                    )
                }
            })
        } else HelperClass.showToast(
            activity, activity.getString(R.string.check_internet_connection)
        )
    }

    override fun onClick(addressModal: AddressModal) {
        this.addressModal = addressModal
        selectAddress =
            addressModal.name + " , " + addressModal.houseNo + " , " + addressModal.landmark + " , " + addressModal.address
        binding.addressTv.text = selectAddress
    }

    override fun onClick(tag: String, date: String) {
        if (TextUtils.equals(SelectTimeAdapter::class.java.simpleName, tag)) selectTime =
            date else if (TextUtils.equals(
                SelectDateAdapter::class.java.simpleName, tag
            )
        ) selectDate = date
    }

    companion object {
        private const val TAG = "BookServicesActivity"
    }
}