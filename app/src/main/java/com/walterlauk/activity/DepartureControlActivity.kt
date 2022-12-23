package com.walterlauk.activity

import android.os.Bundle
import com.walterlauk.R
import com.walterlauk.fragment.DepartureControlFragment


class DepartureControlActivity : BaseActivity() {

    var truckNumber:String =""
    var trailerNumber:String=""
    var truckID: Int = 0
    var trailerID: Int = 0
    var isFromDamage: Boolean = false
    var isTruckShow: Boolean = false
    var isBothSelected: Boolean = false
    var str: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departure_control)

        getBundleData()

        var departureControlFragment = DepartureControlFragment()
        var bundle = Bundle()
        bundle.putInt("truckId", truckID)
        bundle.putInt("trailerId", trailerID)
        bundle.putBoolean("isFromDamage", isFromDamage)
        bundle.putString("truckNumber",truckNumber)
        bundle.putString("trailerNumber",trailerNumber)

        if (isFromDamage) {
            bundle.putBoolean("isTruckShow", isTruckShow)
            bundle.putBoolean("isBothSelected", isBothSelected)
            bundle.putString("str", str)
        }

        departureControlFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.frag_main, departureControlFragment)
            .commit()
    }

    private fun getBundleData() {
        val extras = intent.extras
        if (extras != null) {
            truckID = extras.getInt("truckId")
            trailerID = extras.getInt("trailerId")
            isFromDamage = extras.getBoolean("isFromDamage")
            truckNumber = extras.getString("truckNumber").toString()
            trailerNumber = extras.get("trailerNumber").toString()
            if (isFromDamage) {
                isTruckShow = extras.getBoolean("isTruckShow")
                isBothSelected = extras.getBoolean("isBothSelected")
                str = extras.getString("str")!!
            }
        }
    }
}