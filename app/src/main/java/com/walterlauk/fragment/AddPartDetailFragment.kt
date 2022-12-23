package com.walterlauk.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.walterlauk.R
import com.walterlauk.api.ApiCall
import com.walterlauk.api.FragmentCallback
import com.walterlauk.databinding.FragmentAddPartDetailBinding
import com.walterlauk.models.SuccessResponse
import com.walterlauk.models.TruckTrailer
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants.Companion.BASE_URL
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class AddPartDetailFragment : BaseFragment(), View.OnClickListener, LocationListener {
    var truckTrailerList = ArrayList<TruckTrailer>()
    var truckId: Int = 0
    var trailerId: Int = 0
    var childPosition: Int? = null
    var parentPosition: Int? = null
    var isFromDamage: Boolean = false
    var isTruckClicked: Boolean = false
    lateinit var locationPermission: Array<String>
    lateinit var imagePermission: Array<String>
    var isLatLngSend: Boolean = false
    private val LOCATION_REQUEST_CODE = 1000
    private val IMAGE1_REQUEST_CODE = 1001
    private val IMAGE2_REQUEST_CODE = 1002
    private val IMAGE3_REQUEST_CODE = 1003
    private val IMAGE4_REQUEST_CODE = 1004
    lateinit var fragmentAddPartDetailBinding: FragmentAddPartDetailBinding
    private val REQUEST_CAMERA1 = 51
    private val REQUEST_CAMERA2 = 52
    private val REQUEST_CAMERA3 = 53
    private val REQUEST_CAMERA4 = 54
    private val SELECT_FILE1 = 11
    private val SELECT_FILE2 = 12
    private val SELECT_FILE3 = 13
    private val SELECT_FILE4 = 14
    private var imgGallery1: File? = null
    private var imgGallery2: File? = null
    private var imgGallery3: File? = null
    private var imgGallery4: File? = null
    lateinit var locationManager: LocationManager
    var currentLatitude: Double? = null
    var currentLongitude: Double? = null


    lateinit var fragmentCallback: FragmentCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddPartDetailBinding = FragmentAddPartDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentAddPartDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleData()

        fragmentAddPartDetailBinding.toolbar.tvHeader.text =
            truckTrailerList[parentPosition!!].parts!![childPosition!!].part_name

        fragmentAddPartDetailBinding.imgDamagepart1.setOnClickListener(this)
        fragmentAddPartDetailBinding.imgDamagepart2.setOnClickListener(this)
        fragmentAddPartDetailBinding.imgDamagepart3.setOnClickListener(this)
        fragmentAddPartDetailBinding.imgDamagepart4.setOnClickListener(this)
        fragmentAddPartDetailBinding.btnSubmit.setOnClickListener(this)
        fragmentAddPartDetailBinding.btnClear.setOnClickListener(this)




        if (isFromDamage) {
            locationPermission = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (!checkLocationPermission()) {
                //permission not allowed, request it
                requestLocationPermission()
            } else {
                //permission allowed
                isLatLngSend = true
                getCurrentLatLng()
            }
        }
        /*imagePermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!checkImagePermission()) {
            requestImagePermission()
        }*/
    }

    @JvmName("setFragmentCallback1")
    fun setFragmentCallback(callback: FragmentCallback?) {
        fragmentCallback = callback!!
    }

    private fun requestLocationPermission() {
        requestPermissions(
            locationPermission,
            LOCATION_REQUEST_CODE
        )
    }

    private fun requestImagePermission(IMAGE1_REQUEST_CODE: Int) {
        requestPermissions(
            imagePermission,
            IMAGE1_REQUEST_CODE
        )
    }

    private fun checkLocationPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun checkImagePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLatLng() {
        try {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000L,
                5f,
                this
            )
        } catch (e: SecurityException) {
            println("!!!e.message = ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (grantResults.size > 0) {
                val locationAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                if (locationAccepted) {
                    isLatLngSend = true
                    getCurrentLatLng()
                    /*Toast.makeText(context, "Location Permission accepted", Toast.LENGTH_SHORT)
                        .show()*/
                } else {
                    isLatLngSend = false
                    //Toast.makeText(context, "Location Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            IMAGE1_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val imageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                    if (imageAccepted) {
                        selectImage("image1")
                        //permission accepted
                        /*Toast.makeText(context, "Image1 Permission accepted", Toast.LENGTH_SHORT)
                            .show()*/
                    } else {
                        //permission denied
                        /*Toast.makeText(context, "Image1 Permission denied", Toast.LENGTH_SHORT)
                            .show()*/
                        //Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            IMAGE2_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val image2Accepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                    if (image2Accepted) {
                        selectImage("image2")
                        //permission accepted
                        /*Toast.makeText(context, "Image2 Permission accepted", Toast.LENGTH_SHORT)
                            .show()*/
                    } else {
                        //permission denied
                        /*Toast.makeText(context, "Image2 Permission denied", Toast.LENGTH_SHORT)
                            .show()*/
                        //Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            IMAGE3_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val image3Accepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                    if (image3Accepted) {
                        selectImage("image3")
                        //permission accepted
                        /*Toast.makeText(context, "Image3 Permission accepted", Toast.LENGTH_SHORT)
                            .show()*/
                    } else {
                        //permission denied
                        /*Toast.makeText(context, "Image3 Permission denied", Toast.LENGTH_SHORT)
                            .show()*/
                        //Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            IMAGE4_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val image4Accepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                    if (image4Accepted) {
                        selectImage("image4")
                        //permission accepted
                        /*Toast.makeText(context, "Image4 Permission accepted", Toast.LENGTH_SHORT)
                            .show()*/
                    } else {
                        //permission denied
                        /*Toast.makeText(context, "Image4 Permission denied", Toast.LENGTH_SHORT)
                            .show()*/
                        //Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getBundleData() {
        truckTrailerList = requireArguments().getParcelableArrayList("truckTrailer")!!
        truckId = requireArguments().getInt("truckId")!!
        trailerId = requireArguments().getInt("trailerId")!!
        childPosition = requireArguments().getInt("position")
        parentPosition = requireArguments().getInt("parentPosition")
        isFromDamage = requireArguments().getBoolean("isFromDamage")
        isTruckClicked = requireArguments().getBoolean("isTruckClicked")

        println("!!!truckId = ${truckId}")
        println("!!!tr = ${trailerId}")
        println("!!!truckTrailer = ${truckTrailerList}")
        println("!!!childPosition = ${childPosition}")
        println("!!!parentPosition = ${parentPosition}")
        println("!!!isFromDamage = ${isFromDamage}")
    }

    fun selectImage(isFromImage: String) {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        val pictureDialogItems = arrayOf(
            requireContext().getString(R.string.choose_from_gallery),
            requireContext().getString(R.string.take_photo)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> galleryIntent(isFromImage)
                1 -> cameraIntent(isFromImage)

                /*if (!hasPhoneCameraPermission(Manifest.permission.CAMERA)) {
                requestPermission(
                    Manifest.permission.CAMERA,
                    PERMISSION_REQUEST_CODE_WRITE_CAMERA
                )
            } else {
                cameraIntent(isFromImage)
            }*/
            }
        }
        pictureDialog.show()
    }


    private fun hasPhoneCameraPermission(permission: String): Boolean {
        var ret = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission =
                ContextCompat.checkSelfPermission(requireActivity(), permission)
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
        requestPermissions(requestPermissionArray, requestCode)
    }

    private fun galleryIntent(isFromImage: String) {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (isFromImage.equals("image1")) {
            startActivityForResult(i, SELECT_FILE1)
        } else if (isFromImage.equals("image2")) {
            startActivityForResult(i, SELECT_FILE2)
        } else if (isFromImage.equals("image3")) {
            startActivityForResult(i, SELECT_FILE3)
        } else if (isFromImage.equals("image4")) {
            startActivityForResult(i, SELECT_FILE4)
        }
    }

    private fun cameraIntent(isFromImage: String) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (isFromImage.equals("image1")) {
            startActivityForResult(intent, REQUEST_CAMERA1)
        } else if (isFromImage.equals("image2")) {
            startActivityForResult(intent, REQUEST_CAMERA2)
        } else if (isFromImage.equals("image3")) {
            startActivityForResult(intent, REQUEST_CAMERA3)
        } else if (isFromImage.equals("image4")) {
            startActivityForResult(intent, REQUEST_CAMERA4)
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_damagepart1 -> {

                imagePermission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!checkImagePermission()) {
                    requestImagePermission(IMAGE1_REQUEST_CODE)
                } else {
                    selectImage("image1")
                }
            }
            R.id.img_damagepart2 -> {
                imagePermission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!checkImagePermission()) {
                    requestImagePermission(IMAGE2_REQUEST_CODE)
                } else {
                    selectImage("image2")
                }
            }
            R.id.img_damagepart3 -> {
                imagePermission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!checkImagePermission()) {
                    requestImagePermission(IMAGE3_REQUEST_CODE)
                } else {
                    selectImage("image3")
                }

            }
            R.id.img_damagepart4 -> {
                imagePermission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!checkImagePermission()) {
                    requestImagePermission(IMAGE4_REQUEST_CODE)
                } else {
                    selectImage("image4")
                }
            }
            R.id.btn_submit -> {
                if (!checkValidation()) {
                    fragmentAddPartDetailBinding.prgSubmit.visibility = VISIBLE
                    if (isFromDamage) {
                        callstoreDamageDataApi()
                    } else {
                        callstoreDepartureDataApi()
                    }
                }
            }
            R.id.btn_clear -> {
                clearAllData()
            }
        }
    }

    fun checkValidation(): Boolean {
        if (!fragmentAddPartDetailBinding.chkDamage.isChecked) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.empty_checkbox),
                Toast.LENGTH_SHORT
            )
                .show()
            return true
        } else if (fragmentAddPartDetailBinding.edDesc.text.toString().trim().isEmpty()) {
            Toast.makeText(requireActivity(), getString(R.string.empty_desc), Toast.LENGTH_SHORT)
                .show()
            return true
        }
        return false
    }

    private fun callstoreDamageDataApi() {
        val map = hashMapOf<String, RequestBody>()
        map["driver_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppPref.getValue(AppPref.DRIVER_ID, "").toString()
        )
        var truckTrailerId: RequestBody
        if (isTruckClicked) {
            map["truck_trailer_id"] =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    truckId!!.toString()
                )
        } else {
            map["truck_trailer_id"] =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    trailerId!!.toString()
                )
        }
        map["is_fault"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "1")
        map["category_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            truckTrailerList[parentPosition!!].category_id.toString()
        )
        map["part_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            truckTrailerList[parentPosition!!].parts!![childPosition!!]!!.id.toString()
        )
        map["notes"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            fragmentAddPartDetailBinding.edDesc.text.toString().trim()
        )
        if (fragmentAddPartDetailBinding.chkDamage.isChecked) {
            map["is_part_default"] =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "1")
        } else {
            map["is_part_default"] =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "0")
        }

        if (isLatLngSend) {
            map["lat"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                currentLatitude.toString()
            )
            map["long"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                currentLongitude.toString()
            )
        }
        var imageList = ArrayList<MultipartBody.Part>()

        val requestFile1: RequestBody
        if (imgGallery1 != null) {
            requestFile1 =
                imgGallery1!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery1!!.name, requestFile1)
            )
        }

        val requestFile2: RequestBody
        if (imgGallery2 != null) {
            requestFile2 =
                imgGallery2!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery2!!.name, requestFile2)
            )
        }

        val requestFile3: RequestBody
        if (imgGallery3 != null) {
            requestFile3 =
                imgGallery3!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery3!!.name, requestFile3)
            )
        }

        val requestFile4: RequestBody
        if (imgGallery4 != null) {
            requestFile4 =
                imgGallery4!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery4!!.name, requestFile4)
            )
        }
        ApiCall.initApiCall(BASE_URL).storeDamage(
            "Bearer " + AppPref.getValue(AppPref.TOKEN, "").toString(),
            map, imageList
        ).enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                fragmentAddPartDetailBinding.prgSubmit.visibility = GONE
                if (response.isSuccessful) {
                    if (fragmentCallback != null) {
                        fragmentCallback.onDataSet(
                            true,
                            parentPosition,
                            childPosition,
                            isTruckClicked
                        )
                    }
                    requireActivity()!!.onBackPressed()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                fragmentAddPartDetailBinding.prgSubmit.visibility = GONE
                println("!!!t.messageOnSubmit = ${t.message}")
            }
        })
    }

    private fun callstoreDepartureDataApi() {
        val map = hashMapOf<String, RequestBody>()
        map["driver_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppPref.getValue(AppPref.DRIVER_ID, "").toString()
        )
        if (isTruckClicked) {
            map["truck_trailer_id"] =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    truckId!!.toString()
                )
        } else {
            map["truck_trailer_id"] =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    trailerId!!.toString()
                )
        }
        map["is_fault"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "1")
        map["category_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            truckTrailerList[parentPosition!!].category_id.toString()
        )
        map["part_id"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            truckTrailerList[parentPosition!!].parts!![childPosition!!]!!.id.toString()
        )
        map["notes"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            fragmentAddPartDetailBinding.edDesc.text.toString().trim()
        )
        if (fragmentAddPartDetailBinding.chkDamage.isChecked) {
            map["is_part_default"] =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "1")
        } else {
            map["is_part_default"] =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "0")
        }

        var imageList = ArrayList<MultipartBody.Part>()

        val requestFile1: RequestBody
        if (imgGallery1 != null) {
            requestFile1 =
                imgGallery1!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery1!!.name, requestFile1)
            )
        }

        val requestFile2: RequestBody
        if (imgGallery2 != null) {
            requestFile2 =
                imgGallery2!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery2!!.name, requestFile2)
            )
        }

        val requestFile3: RequestBody
        if (imgGallery3 != null) {
            requestFile3 =
                imgGallery3!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery3!!.name, requestFile3)
            )
        }

        val requestFile4: RequestBody
        if (imgGallery4 != null) {
            requestFile4 =
                imgGallery4!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageList.add(
                MultipartBody.Part.createFormData("images[]", imgGallery4!!.name, requestFile4)
            )
        }
        ApiCall.initApiCall(BASE_URL).storeDeparture(
            "Bearer " + AppPref.getValue(AppPref.TOKEN, "").toString(),
            map, imageList
        ).enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                fragmentAddPartDetailBinding.prgSubmit.visibility = GONE
                if (response.isSuccessful) {
                    if (fragmentCallback != null) {
                        fragmentCallback.onDataSet(
                            true,
                            parentPosition,
                            childPosition,
                            isTruckClicked
                        )
                    }
                    requireActivity()!!.onBackPressed()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                fragmentAddPartDetailBinding.prgSubmit.visibility = GONE
                println("!!!t.messageOnSubmit = ${t.message}")
            }
        })
    }

    private fun clearAllData() {
        if (fragmentAddPartDetailBinding.chkDamage.isChecked) {
            fragmentAddPartDetailBinding.chkDamage.isChecked = false
        }
        fragmentAddPartDetailBinding.edDesc.setText("")

        fragmentAddPartDetailBinding.imgDamagepart1.setImageDrawable(
            requireContext().resources.getDrawable(
                R.drawable.img_bg
            )
        )

        fragmentAddPartDetailBinding.imgDamagepart2.setImageDrawable(
            requireContext().resources.getDrawable(
                R.drawable.img_bg
            )
        )

        fragmentAddPartDetailBinding.imgDamagepart3.setImageDrawable(
            requireContext().resources.getDrawable(
                R.drawable.img_bg
            )
        )

        fragmentAddPartDetailBinding.imgDamagepart4.setImageDrawable(
            requireContext().resources.getDrawable(
                R.drawable.img_bg
            )
        )
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            contentUri!!.path
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    fun getRealPathFromURICamera(uri: Uri?): String? {
        var path = ""
        if (requireActivity().contentResolver != null) {
            val cursor = requireActivity().contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {

            //from gallery
            if (requestCode == SELECT_FILE1) {
                val selectedImageUri = data!!.data
                imgGallery1 = File(getRealPathFromURI(selectedImageUri))
                fragmentAddPartDetailBinding.imgDamagepart1.setImageURI(selectedImageUri)
            } else if (requestCode == SELECT_FILE2) {
                val selectedImageUri = data!!.data
                imgGallery2 = File(getRealPathFromURI(selectedImageUri))
                fragmentAddPartDetailBinding.imgDamagepart2.setImageURI(selectedImageUri)
            } else if (requestCode == SELECT_FILE3) {
                val selectedImageUri = data!!.data
                imgGallery3 = File(getRealPathFromURI(selectedImageUri))
                fragmentAddPartDetailBinding.imgDamagepart3.setImageURI(selectedImageUri)
            } else if (requestCode == SELECT_FILE4) {
                val selectedImageUri = data!!.data
                imgGallery4 = File(getRealPathFromURI(selectedImageUri))
                fragmentAddPartDetailBinding.imgDamagepart4.setImageURI(selectedImageUri)
            }
            //from camera
            else if (requestCode == REQUEST_CAMERA1) {
                /*val image = data!!.extras!!["data"] as Bitmap?
                fragmentAddPartDetailBinding.imgDamagepart1.setImageBitmap(image)*/
                val tempUri = getImageUri(requireActivity(), data!!.extras!!["data"] as Bitmap?)
                imgGallery1 = File(getRealPathFromURICamera(tempUri)!!)
                fragmentAddPartDetailBinding.imgDamagepart1.setImageBitmap(data!!.extras!!["data"] as Bitmap?)
            } else if (requestCode == REQUEST_CAMERA2) {
                val tempUri = getImageUri(requireActivity(), data!!.extras!!["data"] as Bitmap?)
                imgGallery2 = File(getRealPathFromURICamera(tempUri)!!)
                fragmentAddPartDetailBinding.imgDamagepart2.setImageBitmap(data!!.extras!!["data"] as Bitmap?)
            } else if (requestCode == REQUEST_CAMERA3) {
                val tempUri = getImageUri(requireActivity(), data!!.extras!!["data"] as Bitmap?)
                imgGallery3 = File(getRealPathFromURICamera(tempUri)!!)
                fragmentAddPartDetailBinding.imgDamagepart3.setImageBitmap(data!!.extras!!["data"] as Bitmap?)
            } else if (requestCode == REQUEST_CAMERA4) {
                val tempUri = getImageUri(requireActivity(), data!!.extras!!["data"] as Bitmap?)
                imgGallery4 = File(getRealPathFromURICamera(tempUri)!!)
                fragmentAddPartDetailBinding.imgDamagepart4.setImageBitmap(data!!.extras!!["data"] as Bitmap?)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
        println("!!!lat = ${location.latitude}")
        println("!!!lng = ${location.longitude}")
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}