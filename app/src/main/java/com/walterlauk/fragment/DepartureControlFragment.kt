package com.walterlauk.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.*
import com.walterlauk.R
import com.walterlauk.adapter.TrailerPartsAdapter
import com.walterlauk.adapter.TruckPartsAdapter
import com.walterlauk.api.ApiCall
import com.walterlauk.api.FragmentCallback
import com.walterlauk.databinding.FragmentDepartureControlBinding
import com.walterlauk.models.*
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DepartureControlFragment : BaseFragment(), View.OnClickListener, FragmentCallback {

    var truckList = arrayListOf<TruckTrailer>()
    var trailerList = arrayListOf<TruckTrailer>()
    var truckTrailerDataList = arrayListOf<TruckTrailerData>()
    var truckParentDataList = arrayListOf<TruckTrailer>()
    var trailerParentDataList = arrayListOf<TruckTrailer>()
    var truckId: Int = 0
    var trailerId: Int = 0
    var isFromDamage: Boolean = false
    var isTruckShow: Boolean = false
    var isBothSelected: Boolean = false
    var str = ""
    var truckNumber:String =""
    var trailerNumber:String=""

    lateinit var departureControlFragmentBinding: FragmentDepartureControlBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        departureControlFragmentBinding = FragmentDepartureControlBinding.inflate(
            inflater,
            container,
            false
        )
        return departureControlFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        truckId = requireArguments().getInt("truckId") //1 departure  -/ 2 dmanage /
        trailerId = requireArguments().getInt("trailerId")
        isFromDamage = requireArguments().getBoolean("isFromDamage")
        truckNumber = requireArguments().getString("truckNumber").toString()
        trailerNumber = requireArguments().getString("trailerNumber").toString()


        if (AppPref.getValue(AppPref.DRIVER_TYPE, "").equals("Internal")) {
            departureControlFragmentBinding.tvTrailerparts.setOnClickListener(this)
            departureControlFragmentBinding.tvTruckparts.setOnClickListener(this)
        } else {
            departureControlFragmentBinding.tvTrailerparts.setOnClickListener(this)
        }

        if (isFromDamage) {
            departureControlFragmentBinding.toolbar.tvHeader.setText(getString(R.string.damage_control))
            isTruckShow = requireArguments().getBoolean("isTruckShow")
            isBothSelected = requireArguments().getBoolean("isBothSelected")
            str = requireArguments().getString("str")!!
            println("!!!str = ${str}")
            if (str == "Show Trailer") {
                departureControlFragmentBinding.tvTrailerparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTrailerparts.visibility = VISIBLE
                departureControlFragmentBinding.tvTruckparts.visibility = GONE
                departureControlFragmentBinding.elvTruckparts.visibility = GONE
            } else if (str == "show Truck") {
                departureControlFragmentBinding.tvTrailerparts.visibility = GONE
                departureControlFragmentBinding.elvTrailerparts.visibility = GONE
                departureControlFragmentBinding.tvTruckparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTruckparts.visibility = VISIBLE
                departureControlFragmentBinding.tvTruckparts.setTextColor(resources.getColor(R.color.black))
            } else if (str == "") {
                departureControlFragmentBinding.tvTrailerparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTrailerparts.visibility = VISIBLE
                departureControlFragmentBinding.tvTruckparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTruckparts.visibility = GONE
            }

        } else {
            departureControlFragmentBinding.toolbar.tvHeader.setText(getString(R.string.departure_control))
        }
        departureControlFragmentBinding.toolbar.imgBack.setOnClickListener(this)


        /* if (AppPref.getValue(AppPref.DRIVER_TYPE, "").equals("Internal")) {
             departureControlFragmentBinding.tvTruckparts.visibility = View.VISIBLE
             departureControlFragmentBinding.elvTruckparts.visibility = View.VISIBLE
         } else {
             departureControlFragmentBinding.tvTruckparts.visibility = View.GONE
             departureControlFragmentBinding.elvTruckparts.visibility = View.GONE
         }*/



        departureControlFragmentBinding.progress.visibility = VISIBLE
        setExpandableTrailerListview()
        setExpandableTruckListview()
        callTruckTrailerPartsApi()

        departureControlFragmentBinding.elvTrailerparts.setOnChildClickListener(OnChildClickListener { parent, v, parentPosition, childPosition, id -> //get the group header

            var addPartDetailFragment = AddPartDetailFragment()
            addPartDetailFragment.setFragmentCallback(this)
            val bundle = Bundle()
            bundle.putParcelableArrayList("truckTrailer", trailerList)
            bundle.putInt("position", childPosition)
            bundle.putInt("parentPosition", parentPosition)
            bundle.putInt("truckId", truckId)
            bundle.putInt("trailerId", trailerId)
            bundle.putBoolean("isFromDamage", isFromDamage!!)
            addPartDetailFragment.arguments = bundle
            requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_main, addPartDetailFragment)
                .addToBackStack("true")
                .commit()
            false
        })
        departureControlFragmentBinding.elvTrailerparts.setOnGroupClickListener(OnGroupClickListener { parent, v, parentPosition, id -> //get the group header

            false
        })

        departureControlFragmentBinding.elvTruckparts.setOnChildClickListener(OnChildClickListener { parent, v, parentPosition, childPosition, id -> //get the group header

            var addPartDetailFragment = AddPartDetailFragment()
            addPartDetailFragment.setFragmentCallback(this)
            val bundle = Bundle()
            bundle.putParcelableArrayList("truckTrailer", truckList)
            bundle.putInt("position", childPosition)
            bundle.putInt("parentPosition", parentPosition)
            bundle.putInt("truckId", truckId)
            bundle.putInt("trailerId", trailerId)
            bundle.putBoolean("isFromDamage", isFromDamage!!)
            bundle.putBoolean("isTruckClicked", true)
            addPartDetailFragment.arguments = bundle
            requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_main, addPartDetailFragment)
                .addToBackStack("true")
                .commit()
            false
        })
        departureControlFragmentBinding.elvTrailerparts.setOnGroupClickListener(OnGroupClickListener { parent, v, parentPosition, id -> //get the group header

            false
        })

    }

    private fun setExpandableTruckListview() {
        println("TESTING - > expandable ")
        val expandableListTruckAdapter =
            TruckPartsAdapter(requireActivity(), truckParentDataList)
        //departureControlFragmentBinding.elvTruckparts.isNestedScrollingEnabled = true
        departureControlFragmentBinding.elvTruckparts.setAdapter(expandableListTruckAdapter)
    }

    private fun setExpandableTrailerListview() {
        val expandableListTrailerAdapter =
            TrailerPartsAdapter(requireActivity(), trailerParentDataList)
        departureControlFragmentBinding.elvTrailerparts.setAdapter(expandableListTrailerAdapter)
    }

    private fun callTruckTrailerPartsApi() {
        ApiCall.initApiCall(BASE_URL)
            .getTruckTrailerDetails("Bearer " + AppPref.getValue(AppPref.TOKEN,
                "").toString(), trailerNumber,truckNumber)
            .enqueue(object : Callback<ResponseTruckTrailerParts> {
                override fun onResponse(
                    call: Call<ResponseTruckTrailerParts>,
                    response: Response<ResponseTruckTrailerParts>
                ) {
                    departureControlFragmentBinding.progress.visibility = GONE
                    if (response.isSuccessful) {
                        println("Response code ->"+ response.code())
                        if (response.body()!!.status!!) {
                            truckTrailerDataList.add(response.body()!!.data!!)
                            truckList.addAll(response.body()!!.data!!.Truck!!)
                            trailerList.addAll(response.body()!!.data!!.Trailer!!)

                            //parent data
                            for (i in 0 until truckList.size) {
                                truckParentDataList.add(truckList[i])
                            }
                            for (i in 0 until trailerList.size) {
                                trailerParentDataList.add(trailerList[i])
                            }
                            //notify adapter here
                            setExpandableTrailerListview()
                            setExpandableTruckListview()

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseTruckTrailerParts>, t: Throwable) {
                    println("!!!t.message = ${t.message}")
                    departureControlFragmentBinding.progress.visibility = GONE
                }
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                requireActivity().onBackPressed()
            }
            R.id.tv_truckparts -> {


                departureControlFragmentBinding.elvTruckparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTrailerparts.visibility = GONE
                departureControlFragmentBinding.tvTrailerparts.setTextColor(resources.getColor(R.color.darkgrey))
                departureControlFragmentBinding.tvTruckparts.setTextColor(resources.getColor(R.color.black))

            }
            R.id.tv_trailerparts -> {

                departureControlFragmentBinding.elvTrailerparts.visibility = VISIBLE
                departureControlFragmentBinding.elvTruckparts.visibility = GONE
                departureControlFragmentBinding.tvTrailerparts.setTextColor(resources.getColor(R.color.black))
                departureControlFragmentBinding.tvTruckparts.setTextColor(resources.getColor(R.color.darkgrey))

            }
        }
    }

    override fun onDataSet(
        isAdded: Boolean,
        parentPosition: Int?,
        childPosition: Int?,
        isTruckClicked: Boolean
    ) {
        println("###isAddedOnBackPressed = ${isAdded}")
        if (isTruckClicked) {
            truckParentDataList[parentPosition!!].isPartAdded = true
            truckParentDataList[parentPosition!!].parts!![childPosition!!].isPartAdded = true
            setExpandableTruckListview()
        } else {
            trailerParentDataList[parentPosition!!].isPartAdded = true
            trailerParentDataList[parentPosition!!].parts!![childPosition!!].isPartAdded = true
            setExpandableTrailerListview()
        }
    }
}

