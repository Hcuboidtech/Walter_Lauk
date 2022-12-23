package com.walterlauk.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.walterlauk.R
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityProfileBinding
import com.walterlauk.models.ResponseGetUserProfile
import com.walterlauk.models.ResponseLogout
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import com.walterlauk.utils.Constants.Companion.PERMISSION_REQUEST_CODE_WRITE_CAMERA1
import com.walterlauk.utils.Constants.Companion.REQUEST_CAMERA1
import com.walterlauk.utils.Constants.Companion.SELECT_FILE1
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class ProfileActivity : AppCompatActivity() {

    lateinit var profileBinding: ActivityProfileBinding
    lateinit var view: View
    private var userId: String? = null
    private var imgUrl: String? = null
    private var userName: String? = null
    private var imgFirstGallery: File? = null
    var picuri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)

        view = profileBinding.root
        setContentView(view)
        val icview = profileBinding.progressBarInclude
        icview.progressBAR.visibility = View.VISIBLE
        AppPref.init(this)
        userId = AppPref.getValue(AppPref.LOGIN_ID, "")
        userName = AppPref.getValue(AppPref.USER_NAME, "")
        imgUrl = AppPref.getValue(AppPref.PROFILE_IMAGE, "")
        profileBinding.logoutBtn.setOnClickListener {
            logoutHandler()
            picuri = AppPref.getValue(AppPref.PROFILE_IMAGE, "")!!

            if (picuri.equals("")) {
                profileBinding.userProfileImage11.setImageResource(R.drawable.photo_png)
            } else {
                Picasso.get().load(imgUrl).into(profileBinding.userProfileImage11)
            }
        }
        profileBinding.imgBack1.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java))
            onBackPressed()
        }
        profileBinding.updateProfileBtn.setOnClickListener {
            editUserName(profileBinding.userNameEditText.toString(), userName!!, imgUrl!!)
        }

        profileBinding.changePicTextview.setOnClickListener {
            // getImageFromGallery()
            showPictureDialog1()
            // uploadpiccall()
        }
        getUserProfileHandler()
    }

    private fun uploadpiccall() {
        if (!hasPhoneCameraPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Constants.PERMISSION_REQUEST_CODE_Storage
            )
        } else {
            showPictureDialog1()
        }
    }

    private fun hasPhoneCameraPermission(permission: String): Boolean {
        var ret = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission =
                ContextCompat.checkSelfPermission(this@ProfileActivity, permission)
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                ret = true
            }
        } else {
            ret = true
        }
        return ret
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        val requestPermissionArray = arrayOf(permission)
        ActivityCompat.requestPermissions(
            this@ProfileActivity,
            requestPermissionArray,
            requestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE1) {
                val selectedImageUri = data!!.data
                imgFirstGallery = File(getRealPathFromURI(selectedImageUri))
                profileBinding.userProfileImage11.setImageURI(selectedImageUri)
            } else if (requestCode == REQUEST_CAMERA1) {
                onCaptureImageResult1(data!!)
            }
        }

    }

    private fun showPictureDialog1() {
        val pictureDialog = AlertDialog.Builder(this@ProfileActivity)
        val pictureDialogItems = arrayOf(
            getString(R.string.choose_from_gallery),
            getString(R.string.take_photo)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> galleryIntent1() // gallary permision nmeed to ckeck //
                1 -> if (!hasPhoneCameraPermission(Manifest.permission.CAMERA)) {
                    requestPermission(
                        Manifest.permission.CAMERA,
                        PERMISSION_REQUEST_CODE_WRITE_CAMERA1
                    )
                } else {
                    cameraIntent1()
                }
            }
        }
        pictureDialog.show()
    }

    // First Camera
    private fun cameraIntent1() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            intent,
            REQUEST_CAMERA1
        )
    }

    private fun galleryIntent1() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            i,
            SELECT_FILE1
        )
    }

    // Get path form gallery
   private fun getRealPathFromURI(contentUri: Uri?): String? {
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            contentUri!!.path
        }
    }

    // for capture image second
    private fun onCaptureImageResult1(data: Intent) {
        val thumbnail = data.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        profileBinding.userProfileImage11.setImageBitmap(thumbnail)
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        val tempUri = getImageUri(this, thumbnail)
        // CALL THIS METHOD TO GET THE ACTUAL PATH
        imgFirstGallery = File(getRealPathFromURICamera(tempUri)!!)
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    // get path from camera
   private fun getRealPathFromURICamera(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    // for capture image second
    private fun getUserProfileHandler() {
        ApiCall.initApiCall(Constants.BASE_URL)
            .getUserProfile("Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!, userId!!)
            .enqueue(object : Callback<ResponseGetUserProfile> {
                override fun onResponse(
                    call: Call<ResponseGetUserProfile>,
                    response: Response<ResponseGetUserProfile>
                ) {
                    if (response.isSuccessful) {
                        val userName = response.body()?.data?.name
                        val imageUrl = response.body()?.data?.imageName
                        println("username" + "SuccessFull")
                        println("username$userName")
                        println("username$imageUrl")

                        profileBinding.userNameEditText.setText(userName.toString())
                        profileBinding.userNameTextview.text = userName.toString()
                        Picasso.get().load(imageUrl).into(profileBinding.userProfileImage11)
                    }
                }

                override fun onFailure(call: Call<ResponseGetUserProfile>, t: Throwable) {
                }
            })

    }

    private fun editUserName(name: String, user_name: String, imgUrl: String) {
        var itemDocumentFile: MultipartBody.Part? = null
        val requestFile: RequestBody
        if (imgFirstGallery != null) {
            //add
            Toast.makeText(this@ProfileActivity, "Not null", Toast.LENGTH_SHORT).show()
            requestFile = imgFirstGallery!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            itemDocumentFile =
                MultipartBody.Part.createFormData("image_url", imgFirstGallery!!.name, requestFile)
        } else {
            if (picuri != null && !picuri.equals("", ignoreCase = true)) {
                requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), picuri!!)
                itemDocumentFile = MultipartBody.Part.createFormData("image_url", "", requestFile)
            } else {
                requestFile = RequestBody.create(("multipart/form-data").toMediaTypeOrNull(), "")
                itemDocumentFile = MultipartBody.Part.createFormData("image_url", "", requestFile)
            }
        }

        val loginID = userId
        val name1 = profileBinding.userNameEditText.text.toString()
        val user_name1 = user_name

        val addpostRequest = LinkedHashMap<String, RequestBody>()
        addpostRequest["id"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), loginID!!)
        addpostRequest["name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name1)
        addpostRequest["user_name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_name1)

        println("UserId ->$loginID")
        println("Username ->$name1")
        println("Username1->$user_name")

        ApiCall.initApiCall(Constants.BASE_URL).editDriverProfile(
            "Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!,
            addpostRequest, itemDocumentFile
        ).enqueue(
            object : Callback<ResponseLogout> {
                override fun onResponse(
                    call: Call<ResponseLogout>,
                    response: Response<ResponseLogout>
                ) {
                    if (response.isSuccessful) {
                        println("ITEM  " + itemDocumentFile.body.toString())
                        profileBinding.progressBarInclude.progressBAR.visibility = View.INVISIBLE

                        // startActivity(Intent(this@ProfileActivity,DashboardActivity::class.java))
                        Toast.makeText(
                            this@ProfileActivity,
                            response.body()!!.status.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val stat = response.body()!!.message
                        println("status $stat")
                        getUserProfileHandler()
                    }
                }

                override fun onFailure(call: Call<ResponseLogout>, t: Throwable) {
                    println("status1 " + t.message.toString())
                }
            }
        )
    }

    private fun logoutHandler() {
        profileBinding.progressBarInclude.progressBAR.visibility = View.VISIBLE
        val token = AppPref.getValue(AppPref.TOKEN, "")

        if (userId.equals("") || token.equals("")) {
            Toast.makeText(this, "Empty Error", Toast.LENGTH_SHORT).show()
        } else {
            ApiCall.initApiCall(Constants.BASE_URL).logout(userId!!, token!!).enqueue(
                object : Callback<ResponseLogout> {
                    override fun onResponse(
                        call: Call<ResponseLogout>,
                        response: Response<ResponseLogout>
                    ) {
                        if (response.isSuccessful) {
                            profileBinding.progressBarInclude.progressBAR.visibility =
                                View.INVISIBLE
                            AppPref.setValue(AppPref.TOKEN, "")
                            AppPref.setValue(AppPref.LOGIN_ID, "")
                            AppPref.clear()
                            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseLogout>, t: Throwable) {

                        Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }
    }
}
