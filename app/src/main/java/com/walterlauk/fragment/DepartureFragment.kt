package com.walterlauk.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.walterlauk.R
import com.walterlauk.activity.DepartureControlActivity
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.DepartureFragmentBinding
import com.walterlauk.models.ResponseTruckTrailerId
import com.walterlauk.models.SuccessResponse
import com.walterlauk.models.TruckTrailerId
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class DepartureFragment : BaseFragment(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    lateinit var departureFragmentBinding: DepartureFragmentBinding
    var truckTrailerIds = arrayListOf<TruckTrailerId>()

    //var truckTrailerNumber = arrayListOf<String>()
    var truckNumber = arrayListOf<String>()
    var trailerNumber = arrayListOf<String>()
    var check = 0
    var checktrailer = 0
    var truckSelectedItem = ""
    var trailerSelectedItem = ""

    var truckSelectedItemPosition = 0
    var trailerSelectedItemPosition = 0

    var isTruckTrailerIdSelectedOrNot: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        departureFragmentBinding = DepartureFragmentBinding.inflate(
            inflater, container, false
        )
        return departureFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        departureFragmentBinding.rlStart.setOnClickListener(this)

        departureFragmentBinding.spTruckid.onItemSelectedListener = this
        departureFragmentBinding.spTrailerid.onItemSelectedListener = this

        departureFragmentBinding.btnEditExamination.setOnClickListener(this)
        departureFragmentBinding.btnEdit.setOnClickListener(this)

        if (AppPref.getValue(AppPref.DRIVER_TYPE, "").equals("Internal")) {
            departureFragmentBinding.lnTruckid.visibility = VISIBLE
        } else {
            departureFragmentBinding.lnTruckid.visibility = GONE
        }

        departureFragmentBinding.progress.visibility = VISIBLE
        callTruckTrailerIdApi("truck")
        callTruckTrailerIdApi("trailer")


    }

    private fun callTruckTrailerIdApi(type: String) {
        ApiCall.initApiCall(BASE_URL)
            .getTruckTrailerId(
                "Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!,
                200,
                0,
                type
            )
            .enqueue(object : Callback<ResponseTruckTrailerId> {
                override fun onResponse(
                    call: Call<ResponseTruckTrailerId>,
                    response: Response<ResponseTruckTrailerId>
                ) {
                    departureFragmentBinding.progress.visibility = GONE
                    if (response.isSuccessful) {
                        if (response.body()!!.status!!) {
                            truckTrailerIds.addAll(response.body()!!.data!!)
                            if (type == "truck") {
                                for (i in 0 until truckTrailerIds.size) {
                                    if (truckTrailerIds[i].type.equals("Truck")) {
                                        truckNumber.add(truckTrailerIds[i].number!!)
                                    }
                                }

                                val ad1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                    requireActivity(),
                                    android.R.layout.simple_spinner_item,
                                    truckNumber as List<Any?>
                                )
                                ad1.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                                )
                                departureFragmentBinding.spTruckid.setAdapter(ad1)

                                /* val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                     requireActivity(),
                                     android.R.layout.simple_spinner_item,
                                     truckNumber as List<Any?>
                                 )
                                 ad.setDropDownViewResource(
                                     android.R.layout.simple_spinner_dropdown_item
                                 )

                                 departureFragmentBinding.spTruckid.setAdapter(ad)*/
                            } else {
                                for (i in 0 until truckTrailerIds.size) {
                                    if (truckTrailerIds[i].type.equals("Trailer")) {
                                        trailerNumber.add(truckTrailerIds[i].number!!)
                                    }
                                }
                                val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                    requireActivity(),
                                    android.R.layout.simple_spinner_item,
                                    trailerNumber as List<Any?>
                                )
                                ad.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                                )
                                departureFragmentBinding.spTrailerid.setAdapter(ad)
                                /*val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                    requireActivity(),
                                    android.R.layout.simple_spinner_item,
                                    trailerNumber as List<Any?>
                                )
                                ad.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                                )
                                departureFragmentBinding.spTrailerid.setAdapter(ad)*/

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseTruckTrailerId>, t: Throwable) {
                    departureFragmentBinding.progress.visibility = GONE
                }
            })
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_start -> {

                if (isTruckTrailerIdSelectedOrNot) {
                    departureFragmentBinding.progress.visibility = VISIBLE
                    callEditTruckTrailerDetails()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.empty_trailer_id),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.btn_edit_examination -> {
                openDatePicker(
                    requireActivity(),
                    departureFragmentBinding.tvServicedate
                )
            }
            R.id.btn_edit -> {
                openDatePicker(
                    requireActivity(),
                    departureFragmentBinding.tvSafetydate
                )
            }
        }
    }

    private fun callEditTruckTrailerDetails() {
        ApiCall.initApiCall(BASE_URL)
            .editTruckTrailerDetails(
                "Bearer " + AppPref.getValue(AppPref.TOKEN, "").toString(),
                trailerSelectedItem, truckSelectedItem,
                parseDateFormate(
                    departureFragmentBinding.tvServicedate.text.toString().trim(),
                    "dd MMMM,yyyy",
                    "yyyy-MM-dd"
                )!!,
                parseDateFormate(
                    departureFragmentBinding.tvSafetydate.text.toString().trim(),
                    "dd MMMM,yyyy",
                    "yyyy-MM-dd"
                )!!
            ).enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    departureFragmentBinding.progress.visibility = GONE
                    if (response.isSuccessful) {
                        if (response.body()!!.status!!) {
                            startActivity(
                                Intent(activity, DepartureControlActivity::class.java)
                                    .putExtra(
                                        "truckId",
                                        truckTrailerIds[truckSelectedItemPosition].id
                                    ).putExtra("truckNumber",
                                        truckSelectedItem
                                    ).putExtra("trailerNumber",
                                        trailerSelectedItem)
                                    .putExtra(
                                        "trailerId",
                                        truckTrailerIds[trailerSelectedItemPosition].id
                                    )
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    departureFragmentBinding.progress.visibility = GONE
                    println("!!!t.message = ${t.message}")
                }
            })
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val spinner = parent
        if (spinner!!.id == R.id.sp_truckid) {
            if (++check > 0) {
                truckSelectedItemPosition = position
                truckSelectedItem =
                    departureFragmentBinding.spTruckid.getSelectedItem().toString()
                println("TRUCK SELECTED -> "+truckSelectedItem)
            }
        } else if (spinner.id == R.id.sp_trailerid) {
            if (++checktrailer > 0) {
                isTruckTrailerIdSelectedOrNot = true
                trailerSelectedItemPosition = position
                trailerSelectedItem =
                    departureFragmentBinding.spTrailerid.getSelectedItem().toString()

                departureFragmentBinding.progress.visibility = VISIBLE
                println("Trailer Selected "+ trailerSelectedItem)
                callgetTrailerDetails()
            }
        }
    }

    private fun callgetTrailerDetails() {
        ApiCall.initApiCall(BASE_URL).getTruckTrailerDetailsForDate(
            "Bearer " + AppPref.getValue(AppPref.TOKEN, "").toString(), trailerSelectedItem
        ).enqueue(object : Callback<ResponseTruckTrailerId> {
            override fun onResponse(
                call: Call<ResponseTruckTrailerId>,
                response: Response<ResponseTruckTrailerId>
            ) {
                departureFragmentBinding.progress.visibility = GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status!!) {
                        departureFragmentBinding.tvServicedate.text =
                            parseDateFormate(
                                response.body()!!.data!![0].service_date,
                                "yyyy-MM-dd hh:mm:ss",
                                "dd MMMM,yyyy"
                            )
                        departureFragmentBinding.tvSafetydate.text =
                            parseDateFormate(
                                response.body()!!.data!![0].safety_date, "yyyy-MM-dd hh:mm:ss",
                                "dd MMMM,yyyy"
                            )
                    }
                }

            }

            override fun onFailure(call: Call<ResponseTruckTrailerId>, t: Throwable) {
                departureFragmentBinding.progress.visibility = GONE
            }
        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}