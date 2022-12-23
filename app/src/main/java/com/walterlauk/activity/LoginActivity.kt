package com.walterlauk.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.walterlauk.R
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityLoginBinding
import com.walterlauk.models.DriverNodeResponse
import com.walterlauk.models.ResponseLogin
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseNodeForDriverName: DatabaseReference = firebaseDatabase.getReference("")
    lateinit var loginBinding: ActivityLoginBinding
    var deviceToken=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        AppPref.init(this)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                deviceToken = task.result
                println("!!!deviceToken = ${deviceToken}")
            })

        loginBinding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(loginBinding.editTextUserName.text.toString()) ||
                TextUtils.isEmpty(loginBinding.editTextPassword.text.toString())
            ) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.invalid_input),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {

                allowUserToLogin(
                    loginBinding.editTextUserName.text.trim().toString(),
                    loginBinding.editTextPassword.text.trim().toString()
                )
            }
        }
        loginBinding.forgetPasswordTv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
            finish()
        }
    }

    private fun allowUserToLogin(username: String, password: String) {
        loginBinding.loginProgressbar.visibility = View.VISIBLE
        ApiCall.initApiCall(Constants.BASE_URL).login(username, password, "", deviceToken)
            .enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()!!.status!!) {
                            AppPref.setValue(
                                AppPref.TOKEN,
                                response.body()!!.data!!.authorization_token
                            )
                            AppPref.setValue(
                                AppPref.LOGIN_ID,
                                response.body()?.data?.id.toString()
                            )
                            AppPref.setValue(
                                AppPref.USER_NAME,
                                response.body()?.data?.user_name.toString()
                            )
                            AppPref.setValue(
                                AppPref.PROFILE_IMAGE,
                                response.body()?.data?.imageName.toString()
                            )
                            AppPref.setValue(
                                AppPref.FULL_NAME,
                                response.body()!!.data!!.name.toString()
                            )
                            AppPref.setValue(
                                AppPref.DRIVER_TYPE,
                                response.body()!!.data!!.driver_type.toString()
                            )
                            AppPref.setValue(
                                AppPref.DRIVER_ID,
                                response.body()!!.data!!.id.toString()
                            )
                            AppPref.setValue(AppPref.IMAGE_URL,
                                response.body()!!.data!!.image_url.toString())
                            println("image name ->"+response.body()!!.data!!.imageName)

                            checkIfTheUserNodeIsPresentInTheFirebase()
                            loginBinding.loginProgressbar.visibility = View.INVISIBLE
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            loginBinding.loginProgressbar.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        loginBinding.loginProgressbar.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            response.errorBody()!!.string(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    loginBinding.loginProgressbar.visibility = View.GONE
                    println("!!!t.messageLogin = ${t.message}")
                    Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    fun checkIfTheUserNodeIsPresentInTheFirebase(){

        var test = DriverNodeResponse()
        var arrayList = ArrayList<String>()
        databaseNodeForDriverName.child("Drivers")
            .addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    println("SNAPSHOTEXIST")
                }else{
                    println("SNAPSHOT is NOT EXIST")
                    setUpTheFirebaseNode()
                }
                println("snapshotvalue"+"")
                for (ds in snapshot.children){
                    println("VALUE _>>"+ snapshot.value)
                     test= ds.getValue(DriverNodeResponse::class.java)!!
                    // arrayListOfReceivedMessage.add(test)
                    arrayList.add(test.id)
                }
                for (item in arrayList){
                    println("item in the list ->"+item) //conversation_admin_ //conversation_admin_123_
                    if (item.contains(AppPref.getValue(AppPref.USER_NAME,"").toString())){
                        println("NAME FOUND ")
                        break

                    }else{
                        println("No Node Found")
                      setUpTheFirebaseNode()
                       break
                    }
                }  // loop
                println("ID ->>"+arrayList)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

   ///   this function is used for creating the Driver for the admin panel chat detection
    fun setUpTheFirebaseNode(){
        val hashMap = HashMap<String,String>()
        hashMap.put("emailAddress",AppPref.getValue(AppPref.USER_NAME,"").toString())
        hashMap.put("fullName",AppPref.getValue(AppPref.FULL_NAME,"").toString())
        hashMap.put("profile_pic",AppPref.getValue(AppPref.IMAGE_URL,"").toString())
       println("Setup The Firebase Node is called")
        databaseNodeForDriverName.child("Drivers")
            .child(AppPref.getValue(AppPref.USER_NAME,"").toString())
            .setValue(hashMap).addOnCompleteListener {
                println("saved -> node")
            }
    }
}