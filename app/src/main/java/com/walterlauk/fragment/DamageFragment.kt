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
import androidx.core.content.ContextCompat
import com.walterlauk.R
import com.walterlauk.activity.DepartureControlActivity
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.DamageFragmentBinding
import com.walterlauk.databinding.DepartureFragmentBinding
import com.walterlauk.models.ResponseTruckTrailerId
import com.walterlauk.models.SuccessResponse
import com.walterlauk.models.TruckTrailerId
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DamageFragment : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
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
    var isTruckTrailerSelectedOrNot = false
    var isTruckSelected = true
    var isTrailerSelected = true
    var isTruckShow = false
    var isBothSelected = false

    lateinit var damageFragmentBinding: DamageFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        damageFragmentBinding = DamageFragmentBinding.inflate(inflater, container, false)
        return damageFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        damageFragmentBinding.spTruckid.onItemSelectedListener = this
        damageFragmentBinding.spTrailerid.onItemSelectedListener = this

        damageFragmentBinding.rlNext.setOnClickListener(this)

        damageFragmentBinding.imgTruck.setOnClickListener(this)
        damageFragmentBinding.imgTrailer.setOnClickListener(this)

        if (AppPref.getValue(AppPref.DRIVER_TYPE, "").equals("Internal")) {
            //visible truck stuff
            damageFragmentBinding.imgTruck.setBackgroundResource(R.drawable.ic_blue_bg_damage)
            damageFragmentBinding.imgTruck.setImageDrawable(requireContext()!!.getDrawable(R.drawable.ic_truck_image))
            damageFragmentBinding.lnTruckid.visibility = VISIBLE
            damageFragmentBinding.imgTruck.isEnabled = true
        } else {
            //hide truck stuff
            damageFragmentBinding.imgTruck.setBackgroundResource(R.drawable.ic_truck_not_selected_grey)
            damageFragmentBinding.imgTruck.setImageDrawable(requireContext()!!.getDrawable(R.drawable.ic_truck_not_selected))
            damageFragmentBinding.lnTruckid.visibility = GONE
            damageFragmentBinding.imgTruck.isEnabled = false
        }

        damageFragmentBinding.progress.visibility = VISIBLE
        callTruckTrailerIdApi("truck")
        callTruckTrailerIdApi("trailer")
    }

    private fun callTruckTrailerIdApi(type: String) {
        ApiCall.initApiCall(Constants.BASE_URL)
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
                    damageFragmentBinding.progress.visibility = GONE
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
                                damageFragmentBinding.spTruckid.setAdapter(ad1)

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
                                damageFragmentBinding.spTrailerid.setAdapter(ad)
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
                    damageFragmentBinding.progress.visibility = GONE
                }
            })
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_next -> {

                if (isTruckSelected || isTrailerSelected) {

                    var str = ""
                    if (!isTruckSelected) {//trailer selected
                        str = "Show Trailer"
                    } else if (isTruckSelected && isTrailerSelected) {
                        str = ""
                    } else {
                        str = "show Truck"
                    }

                    startActivity(
                        Intent(activity, DepartureControlActivity::class.java)
                            .putExtra("truckId", truckTrailerIds[truckSelectedItemPosition].id)
                            .putExtra("trailerId", truckTrailerIds[trailerSelectedItemPosition].id)
                            .putExtra("isFromDamage", true)
                            .putExtra("isTruckShow", isTruckShow)
                            .putExtra("isBothSelected", isBothSelected)
                            .putExtra("str", str)

                    )
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please select truck or trailer id",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            R.id.img_truck -> {

                if (!isTruckSelected) {
                    isTruckSelected = true
                    damageFragmentBinding.imgTruck.setBackgroundResource(R.drawable.ic_blue_bg_damage)
                    damageFragmentBinding.imgTruck.setImageDrawable(requireContext()!!.getDrawable(R.drawable.ic_truck_image))
                    damageFragmentBinding.lnTruckid.visibility = VISIBLE
                } else {
                    isTruckSelected = false
                    damageFragmentBinding.imgTruck.setBackgroundResource(R.drawable.ic_truck_not_selected_grey)
                    damageFragmentBinding.imgTruck.setImageDrawable(requireContext()!!.getDrawable(R.drawable.ic_truck_not_selected))
                    damageFragmentBinding.lnTruckid.visibility = GONE
                }
            }
            R.id.img_trailer -> {

                if (!isTrailerSelected) {
                    isTrailerSelected = true
                    damageFragmentBinding.imgTrailer.setBackgroundResource(R.drawable.ic_blue_bg_damage)
                    damageFragmentBinding.imgTrailer.setImageDrawable(
                        requireContext()!!.getDrawable(
                            R.drawable.ic_trailer_image
                        )
                    )
                    damageFragmentBinding.lnTrailerId.visibility = VISIBLE

                } else {
                    isTrailerSelected = false
                    damageFragmentBinding.imgTrailer.setBackgroundResource(R.drawable.ic_truck_not_selected_grey)
                    damageFragmentBinding.imgTrailer.setImageDrawable(
                        requireContext()!!.getDrawable(
                            R.drawable.ic_trailer_not_selected
                        )
                    )
                    damageFragmentBinding.lnTrailerId.visibility = GONE
                }


            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val spinner = parent
        if (spinner!!.id == R.id.sp_truckid) {
            if (++check > 0) {
                isTruckTrailerSelectedOrNot = true
                truckSelectedItemPosition = position
                truckSelectedItem =
                    damageFragmentBinding.spTruckid.getSelectedItem().toString()
            }
        } else if (spinner.id == R.id.sp_trailerid) {
            if (++checktrailer > 0) {
                isTruckTrailerSelectedOrNot = true
                trailerSelectedItemPosition = position
                trailerSelectedItem =
                    damageFragmentBinding.spTrailerid.getSelectedItem().toString()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}