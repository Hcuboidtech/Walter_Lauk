package com.walterlauk.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walterlauk.R
import com.walterlauk.adapter.ContactUsAdapter
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityContactUsBinding
import com.walterlauk.models.ResponseTeams
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactUsActivity : BaseActivity(), View.OnClickListener {

    lateinit var contactUsBinding: ActivityContactUsBinding
    lateinit var callPermission: Array<String>
    var isPermissionallowed = false
    private val CALL_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactUsBinding = ActivityContactUsBinding.inflate(layoutInflater)
        val view = contactUsBinding.root
        setContentView(view)

        contactUsBinding.toolbar.imgBack.setOnClickListener(this)
        contactUsBinding.toolbar.tvHeader.setText(getString(R.string.contact_us))

        callPermission = arrayOf(Manifest.permission.CALL_PHONE)

        if (!checkCallPermission()) {
            //permission not allowed, request it
            requestCallPermission()
        } else {
            //permission allowed
            isPermissionallowed = true
        }

        contactUsBinding.progress.visibility = VISIBLE
        callGetTeamApi()
    }

    private fun checkCallPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun requestCallPermission() {
        ActivityCompat.requestPermissions(this, callPermission, CALL_REQUEST_CODE)
    }

    private fun callGetTeamApi() {
        ApiCall.initApiCall(BASE_URL)
            .getTeams("Bearer " + AppPref.getValue(AppPref.TOKEN, "").toString())
            .enqueue(object : Callback<ResponseTeams> {
                override fun onResponse(
                    call: Call<ResponseTeams>,
                    response: Response<ResponseTeams>
                ) {
                    contactUsBinding.progress.visibility = GONE
                    if (response.isSuccessful) {
                        if (response.body()!!.status!!) {
                            for (i in 0 until response.body()!!.data!!.size) {
                                val vi = applicationContext.getSystemService(
                                    LAYOUT_INFLATER_SERVICE
                                ) as LayoutInflater
                                val v: View = vi.inflate(R.layout.layout_contactus, null)
                                val tv_role = v.findViewById<TextView>(R.id.tv_role)
                                val rv_data = v.findViewById<RecyclerView>(R.id.rv_data)
                                if (response.body()!!.data!![i].data!!.size == 0) {
                                    tv_role.visibility = GONE
                                    rv_data.visibility = GONE
                                } else {
                                    tv_role.visibility = VISIBLE
                                    rv_data.visibility = VISIBLE
                                    tv_role.text = response.body()!!.data!![i].main_role

                                    var contactUsAdapter = ContactUsAdapter(
                                        this@ContactUsActivity,
                                        response.body()!!.data!![i].data,isPermissionallowed
                                    )
                                    var layoutManager = LinearLayoutManager(this@ContactUsActivity)
                                    rv_data.layoutManager = layoutManager
                                    rv_data.isNestedScrollingEnabled = false
                                    rv_data.adapter = contactUsAdapter
                                }

                                val insertPoint = findViewById<View>(R.id.ln_main) as LinearLayout
                                insertPoint.addView(
                                    v,
                                    i,
                                    ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseTeams>, t: Throwable) {
                    contactUsBinding.progress.visibility = GONE
                    println("!!!t.message = ${t.message}")
                }
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CALL_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val callAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                    if (callAccepted) {
                        isPermissionallowed = true
                    } else {
                        isPermissionallowed = false
                    }
                }
            }
        }
    }
}