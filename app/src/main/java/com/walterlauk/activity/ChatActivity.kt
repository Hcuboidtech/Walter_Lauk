package com.walterlauk.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.walterlauk.R
import com.walterlauk.adapter.ChatAdapter
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.ActivityChatBinding
import com.walterlauk.models.*
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : BaseActivity(), View.OnClickListener {
    var STORAGE_PERMISSION_CODE =1
    var sender_email =""
    var progressBar:ProgressBar? =null
    var videoClicked =""
    var admin123_indexForInserting=""
    var nodeName =""
    var currentDateandTime =""
    var cursor =""
    var arrayListOfReceivedMessage = ArrayList<Test>()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var database: DatabaseReference = firebaseDatabase.getReference("")
    var databaseReferenceForDriver: DatabaseReference = firebaseDatabase.getReference("")

    private var imgFirstGallery: File? = null
    var imageEnabled =0
    private lateinit var chatBinding: ActivityChatBinding
     var arrayList = ArrayList<ChatModel>()
     var messageTyped = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatBinding = ActivityChatBinding.inflate(layoutInflater)
        val view = chatBinding.root
        setContentView(view)
        AppPref.init(this)
        println("User PREF USer name ->"+
                AppPref.getValue(AppPref.USER_NAME,"")
        )
        println("UserFull name"+AppPref.getValue(AppPref.FULL_NAME,""))
        chatBinding.sendImageBtn.setOnClickListener(this)
        chatBinding.inputChatText.setOnClickListener(this)
        chatBinding.btnSendChat.setOnClickListener(this)
        chatBinding.sendVideoBtn.setOnClickListener(this)

        arrayListOfReceivedMessage.clear()
        // get the messages list from the firebase  //
        println("NODE MANE IS 1->"+AppPref.getValue(AppPref.MESSAGE_NODE,""))

        if (AppPref.getValue(AppPref.MESSAGE_NODE,"").equals("")){
          println("node is empty -----")
          handleAfterAppIsUninstalled()
        }else{
            checkIfUserIsSendingTheFirstMessage()
            readTheDataFromTheDatabase()
        }
        println("NODE MANE IS ->"+AppPref.getValue(AppPref.MESSAGE_NODE,""))
       //
      chatBinding.include.imgBack.setOnClickListener {
          onBackPressed()
      }


    }

    fun showTheProgressBar(isShow:Boolean){
        progressBar = findViewById(R.id.progress_bar_chat)
        if (isShow){

            progressBar!!.visibility = View.VISIBLE
            chatBinding.chatRecyclerView.visibility = View.INVISIBLE
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }else{
            progressBar!!.visibility = View.GONE
            chatBinding.chatRecyclerView.visibility = View.VISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

     private fun handleAfterAppIsUninstalled(){
          val arrayList = ArrayList<String>()
         println("handle app after uninstall function called")
          databaseReferenceForDriver.child("Drivers")
             .child("admin_123").child("conversations")
              .addListenerForSingleValueEvent(object :ValueEventListener{
                  override fun onDataChange(snapshot: DataSnapshot) {
                      for (ds in snapshot.children){
                          println("value 1 ->"+ds.child("id").value)
                          println("VALUE _>>"+ snapshot.value)
//                            test= ds.getValue(DriverChatAdmin_123Response::class.java)!!
                         // arrayListOfReceivedMessage.add(test)
                         arrayList.add(ds.child("id").value.toString())   //ds.value.toString() //old
                      }
                      for (item in arrayList){
                          println("item in the list ->$item") //conversation_admin_ //conversation_admin_123_
                        if (item.contains(Constants.CHAT_DEFAULT_NODE_NAME+AppPref.getValue(AppPref.USER_NAME,""))){
                            println("YES IT GOT THE LIST of message and the node at driver ref ->$item")
                            AppPref.setValue(AppPref.MESSAGE_NODE,item)
                            nodeName = item
                        }else{
                            println("No Node Found")
                           //checkIfUserIsSendingTheFirstMessage()
                        }
                      }  // loop
                println("ID ->>$arrayList")
                    checkIfUserIsSendingTheFirstMessage()
                    readTheDataFromTheDatabase()
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }
              })
     }
    private fun checkIfUserIsSendingTheFirstMessage(){
              //// until app is not  uninstalled //
        if (AppPref.getValue(AppPref.MESSAGE_NODE,"").equals("")){
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            currentDateandTime = sdf.format(Date())
            println("modified$currentDateandTime")
            println("User node is not present  ->  user is new ")
             nodeName = Constants.CHAT_DEFAULT_NODE_NAME+ AppPref.getValue(AppPref.USER_NAME,"")+"_"+ currentDateandTime
             println(AppPref.getValue(AppPref.USER_NAME,"")+" this is the full name")
            database= database.child("Conversations").child(nodeName).child("messages")
            AppPref.setValue(AppPref.MESSAGE_NODE,nodeName)

            // handle uninstall /// from the driver node
           databaseReferenceForDriver = databaseReferenceForDriver.child("Drivers")
                .child("admin_123").child("conversations")

        }else{
            println("User Already registered with the firebase database")

            database= database.child("Conversations").child(AppPref.getValue(AppPref.MESSAGE_NODE,"")!!)
                .child("messages")  // this is for the user node
            databaseReferenceForDriver = databaseReferenceForDriver.child("Drivers")
                .child("admin_123").child("conversations")
        }


    }


    private fun setAdapter() {
        println("SET ADAPTER CALLED1")
        val chatAdapter = ChatAdapter(arrayListOfReceivedMessage,this,object :OnItemClickedListeners{
            override fun addNewItem(string: String) {
                chatBinding.inputChatText.setText("")
            }
        })
        chatBinding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatBinding.chatRecyclerView.adapter = chatAdapter
        chatBinding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            R.id.send_image_btn->{
                imageEnabled =2
                // check if the permission is granted or not //
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,1)
                println("BUTTON CLICKED  ->  image BUTTON ")
            }

            R.id.send_video_btn ->{

                println("BUTTON CLICKED  ->  video BUTTON ")
                if(ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this
                        ,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)

                }else{
                    println("clicked ------------- permision")
                    handleVideoPick()
                    videoClicked ="yes"
                }
            }

            R.id.input_chat_text ->{
                println("BUTTON CLICKED  ->  input chat text BUTTON ")

            }

            R.id.btn_send_chat ->{
                println("BUTTON CLICKED  ->  send  chat BUTTON ")

              if (!TextUtils.isEmpty(chatBinding.inputChatText.text)){
                messageTyped = chatBinding.inputChatText.getText().toString()
                  arrayListOfReceivedMessage.clear()
                getTheLastIndexValueOfMessage(messageTyped,"","")
                  chatBinding.inputChatText.setText("")
               //   sender_email = AppPref.getValue(AppPref.EMAIL,"").toString()
                 val username = AppPref.getValue(AppPref.USER_NAME,"").toString()
                  println("Sender email$sender_email")
                  callSendNotificationToAdminApi(username,"admin_123",messageTyped)
                 }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@ChatActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) ===
                                PackageManager.PERMISSION_GRANTED)) {
                     //   Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                     // here is open the gallery intent to pick up the image from the Directory  //
                        galleryIntent1()

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    private fun handleSendChat(){
     setAdapter()
    }

    private fun galleryIntent1() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            i,
            Constants.SELECT_FILE1
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

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        val requestPermissionArray = arrayOf(permission)
        ActivityCompat.requestPermissions(
            this@ChatActivity,
            requestPermissionArray,
            requestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE1) {
                val selectedImageUri = data!!.data
                imgFirstGallery = File(getRealPathFromURI(selectedImageUri))
                messageTyped = selectedImageUri.toString()
                println("RETURN12 ->")
                println("RETURN ->"+sendImagesToTheCPanelToGetUrl(imgFirstGallery!!))
                handleSendChat()

            // image data is fetched here
            //    chatBinding.imagetemp.setImageURI(selectedImageUri)
            } else if (requestCode == Constants.REQUEST_CAMERA1) {
            }

            if (requestCode === 900) {
                val selectedImageUri: Uri? = data!!.data

                // OI FILE Manager
                val filemanagerstring = selectedImageUri!!.path
                println("FILEMANAGER ->"+filemanagerstring)

                // MEDIA GALLERY
               val  selectedImagePath = File(getRealPathFromURI(selectedImageUri))
                imgFirstGallery = selectedImagePath
                println("selected IMage path"+selectedImagePath)
                val uri = Uri.parse(
                    Environment.getExternalStorageDirectory().toString() + selectedImagePath
                )

                sendImagesToTheCPanelToGetUrl(selectedImagePath)
            }

        }

    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    private fun handleVideoPick(){
        println("video pick clicked")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),900
        )
    }

    fun insertMessageToFirebaseDatabase(lastIndexNode: String, messageTyped: String){
        arrayListOfReceivedMessage.clear()
        var idForTheInsertingMessage =AppPref.getValue(AppPref.MESSAGE_NODE,"").toString()
         idForTheInsertingMessage=idForTheInsertingMessage.replace("conversation_","")

        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        currentDateandTime = sdf.format(Date())
        println("insert message to firebase called")
        println("last index is +1 ->$lastIndexNode")
        val hashMap = HashMap<String,Any>()
            hashMap.put("content",messageTyped)
            hashMap.put("date",currentDateandTime)
            hashMap.put("id",idForTheInsertingMessage )
            hashMap.put("is_read",false)
            hashMap.put("name","Admin")
            hashMap.put("sender_email",AppPref.getValue(AppPref.USER_NAME,"").toString())
            hashMap.put("thumbnail","empty")
            hashMap.put("type","text")
           database.child(lastIndexNode).setValue(hashMap).addOnSuccessListener{
                println("SUCCESS FULL INSERTED  = >>$nodeName")
              setTheLatestMessageONDriverUniqueNode()
              savedDataOnTheAdmin123()
                handleSendChat()
               // saving data on the driver node
            }
    }

    // this function is used for reading the messages from the server once user open the chat
   private fun readTheDataFromTheDatabase(){
       println("read the data from the database")
     arrayListOfReceivedMessage.clear()

       database.addValueEventListener(object :ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               arrayListOfReceivedMessage.clear()
               var test =Test()
               println("RECEIVED ->"+snapshot.value)
              for (ds in snapshot.children){
                  println("VALUE _>>"+ snapshot.value)
                   test= ds.getValue(Test::class.java)!!

                   arrayListOfReceivedMessage.add(test)
              }
               if (snapshot.value ==""){
                   cursor ="0"
               }
               arrayList.add(ChatModel("","",false))
               setAdapter()

           }

           override fun onCancelled(error: DatabaseError) {
               println("CANCELED")
           }
       })
   }


    //this fun give the last item from the message node
    // so that next value will be inserted as the +1 next to
    //it
    private fun getTheLastIndexValueOfMessage(messageTyped: String,imageSend:String,videoUri:String):String{

        println("get the last index value of message")
        val query = database.orderByKey().limitToLast(1)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                println("get the last index value of message2")
                for(ds in snapshot.children){
                    var node  = ds.key?.toInt()
                    node = node?.plus(1)
                    cursor = node.toString()
                    // here inserting the values into the firebase
                println("CURSOR ->$cursor")

                    if (imageSend != ""){
                        println("called ->")
                      insertImageToTheDataBase(imageSend,videoUri)
                    }else {
                        insertMessageToFirebaseDatabase(cursor, messageTyped)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            println("error ->"+error.message)
                println("get the last index value of message3")
            }
        })
          if (cursor == ""){
              cursor ="0"
              println("cursor is 0 ->")
              insertMessageToFirebaseDatabase(cursor,messageTyped)
               arrayListOfReceivedMessage.clear()
          }
        return cursor


    }

    //// this function will provide u the URL of the image  ///
    private fun sendImagesToTheCPanelToGetUrl(imgFirstGallery: File) :String {
        showTheProgressBar(true)

        var imgURL =""
            var itemDocumentFile: MultipartBody.Part? = null

            println(" - > Saved Images to the server  - >")

            val requestFile: RequestBody = this.imgFirstGallery!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            itemDocumentFile =
                MultipartBody.Part.createFormData("imgvideo", this.imgFirstGallery!!.name, requestFile)


          ApiCall.initApiCall(Constants.BASE_URL).saveImage(
         "Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!
         ,itemDocumentFile).enqueue(object :Callback<ResponseSavedImage>{
         override fun onResponse(
             call: Call<ResponseSavedImage>,
             response: Response<ResponseSavedImage>
         ) {
           if (response.isSuccessful){
               println("Saved Response Is SUCCESSFUL")
               if(response.code() == 200){
                   println("Saved Response Code Is SUCCESSFUL")
                   imgURL = response.body()?.data?.imgvideoUrl.toString()
                    println("RETURN api ->$imgURL")
                   arrayList.add(ChatModel(imgURL,"",true))
                      getTheLastIndexValueOfMessage("",imgURL,videoClicked)
                     // this is the image URL USER send threw chat to admin
                   // arrayList.add(ChatModel(imgURL,"",true))
                showTheProgressBar(false)
               }
           }else{
               println("Saved Response is Not Successful")
               showTheProgressBar(false)

           }
         }
         override fun onFailure(call: Call<ResponseSavedImage>, t: Throwable) {
             showTheProgressBar(false)
             println("Saved Image Response is Failure"+ t.message)
             Toast.makeText(this@ChatActivity, "Failed", Toast.LENGTH_SHORT).show()

         }
     })
        println("RETURN api end ->$imgURL")
    return imgURL
}


    // this function will insert the message and the videos related

     fun insertImageToTheDataBase(imgURL: String,videoUri: String) {

         println("insert to firebase called")
         println("insert image to firebase called")
         println("last index is +1 ->$cursor")
         println("setTheLatestMessageONDriverUniqueNode -- called")

         val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
         currentDateandTime = sdf.format(Date())
         println("modified$currentDateandTime")
         val hashMap = HashMap<String,Any>()
         hashMap.put("content",imgURL)
         hashMap.put("date",currentDateandTime)
         hashMap.put("id",AppPref.getValue(AppPref.MESSAGE_NODE,"").toString())
         hashMap.put("is_read",false)
         hashMap.put("name","Admin")
         hashMap.put("sender_email",AppPref.getValue(AppPref.USER_NAME,"").toString())
         hashMap.put("thumbnail","")
         if (videoUri !=""){
             sender_email = AppPref.getValue(AppPref.USER_NAME,"").toString()

             callSendNotificationToAdminApi(sender_email,"admin_123","video")
             println("URI SEND AS VIDEO")
             hashMap.put("type","video")
         }else {
             sender_email = AppPref.getValue(AppPref.USER_NAME,"").toString()

             callSendNotificationToAdminApi(sender_email,"admin_123","image")
             println("URI SEND AS image")
             hashMap.put("type", "photo")
         }
         database.child(cursor).setValue(hashMap).addOnSuccessListener{
             println("SUCCESS FULL INSERTED  = >>"+nodeName)
             videoClicked =""
         }
     }


    private fun setTheLatestMessageONDriverUniqueNode(){
        println("setTheLatestMessageONDriverUniqueNode -- called")
        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        currentDateandTime = sdf.format(Date())
        println("modified$currentDateandTime")

        val hashMapForConversations =HashMap<String,String>()
        hashMapForConversations.put("id",AppPref.getValue(AppPref.MESSAGE_NODE,"").toString())
        hashMapForConversations.put("name","Admin")
        hashMapForConversations.put("other_user_email","admin_123")

        val hashMapForTheLatestMessage =HashMap<String,Any>()
             hashMapForTheLatestMessage.put("date",currentDateandTime)
             hashMapForTheLatestMessage.put("isRead",false)
             hashMapForTheLatestMessage.put("message",messageTyped)
        val firebaseDatabaseDriverUniqueNode = firebaseDatabase.getReference("")

        // this is for the driver conversation
        firebaseDatabaseDriverUniqueNode.child("Drivers")
            .child(AppPref.getValue(AppPref.USER_NAME,"").toString())
            .child("conversations")
            .child("0")
            .setValue(hashMapForConversations).addOnSuccessListener {
                println("Data added on the DRIVER - conversation 0 --")
            }
        // this is for the latest message for the firebase //
        firebaseDatabaseDriverUniqueNode.child("Drivers")
            .child(AppPref.getValue(AppPref.USER_NAME,"").toString())
            .child("conversations")
            .child("0")
            .child("latest_message").setValue(hashMapForTheLatestMessage).addOnSuccessListener {
                println("Data added on the DRIVER - CONVERSATION - latest_message")
            }
    }

    private fun savedDataOnTheAdmin123(){
        var index =-1
       // readDataOnDriverAdmin123Node(nodeName)
        println("Saved data on the driver node is called"+AppPref.getValue(AppPref.MESSAGE_NODE,""))
        val indexForTheAdmin_123 = ArrayList<Int>()
        val listOF_the_ID_FROM_the_admin_123 = ArrayList<String>()

        databaseReferenceForDriver.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (ds in snapshot.children) {
                        println("the value ->" + ds.child("id").value)
                        if(ds.child("id").value == null){
                            admin123_indexForInserting ="0"
                            println(admin123_indexForInserting+"admin123_indexForInserting")
                            insertTheValuesAtAdmin_123()
                            break
                        }else{
                            println("The key - > " + ds.key)
                            indexForTheAdmin_123.add(ds.key!!.toInt())
                            listOF_the_ID_FROM_the_admin_123.add(ds.child("id").value.toString())
                        }
                    }
                    if (admin123_indexForInserting ==""){
                        for (item in listOF_the_ID_FROM_the_admin_123) {
                            index++
                            if (item == AppPref.getValue(AppPref.MESSAGE_NODE, "").toString()) {
                                admin123_indexForInserting =
                                    listOF_the_ID_FROM_the_admin_123.indexOf(item).toString()
                                println("admin 123 -index for inserting $admin123_indexForInserting")
                                insertTheValuesAtAdmin_123()
                                break
                            } else {
                                println("admin 123 -index for inserting 11$admin123_indexForInserting")
                              //  admin123_indexForInserting = indexForTheAdmin_123.size.toString()
                              if (index == listOF_the_ID_FROM_the_admin_123.size-1){
                                 index++
                                  admin123_indexForInserting = index.toString()
                                  insertTheValuesAtAdmin_123()
                              }
                            }
                        }
                    }

                }else{
                    admin123_indexForInserting ="0"
                    insertTheValuesAtAdmin_123()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("error ->" + error.message)
                println("get the last index value of message3")
            }
        })//////

    }
    fun insertTheValuesAtAdmin_123(){

            println("admin idnex ->"+admin123_indexForInserting)
            println(admin123_indexForInserting+"admin123_indexForInserting")

            println("saved data on the admin 123 called")
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            currentDateandTime = sdf.format(Date())
            println("modified$currentDateandTime")

            val hashMapForConversations =HashMap<String,String>()
            hashMapForConversations.put("id",AppPref.getValue(AppPref.MESSAGE_NODE,"").toString())
            hashMapForConversations.put("name","Admin")
            hashMapForConversations.put("other_user_email","admin_123")

            val hashMapForTheLatestMessage =HashMap<String,Any>()
            hashMapForTheLatestMessage.put("date",currentDateandTime)
            hashMapForTheLatestMessage.put("is_read",false)
            hashMapForTheLatestMessage.put("message",messageTyped)
            val firebaseDatabaseDriverAdmin123Node = firebaseDatabase.getReference("")

            firebaseDatabaseDriverAdmin123Node.child("Drivers").child("admin_123")
                .child("conversations").child(admin123_indexForInserting)
                .setValue(hashMapForConversations).addOnSuccessListener {
                    println("------ data is inserted at admin_123 ------")
                }
            firebaseDatabaseDriverAdmin123Node.child("Drivers").child("admin_123")
                .child("conversations").child(admin123_indexForInserting)
                .child("latest_message").setValue(hashMapForTheLatestMessage).addOnSuccessListener {
                    println("----- data is inserted at admin_123 ---- latest message")
                }
            // loop
            println("ID ->>$arrayList")
         //setAdapter()

    }
    private fun callSendNotificationToAdminApi(senderEmail:String, username:String, message:String){
        println("Notification Send Api called")
        ApiCall.initApiCall(Constants.BASE_URL).sendNotificationToAdmin(
            senderEmail,username,message
        ).enqueue(object :Callback<SendNotificatioToAdminResponse>{
            override fun onResponse(
                call: Call<SendNotificatioToAdminResponse>,
                response: Response<SendNotificatioToAdminResponse>
            ) {
                if (response.isSuccessful){
                    if (response.code() == 200) {
                        println("Notification send" +
                                "Successful")
                    println("response"+response.body()!!.data.toString())
                    }else{
                        println("Notification Api Code ->"+response.code())
                    }
                }
            }

            override fun onFailure(call: Call<SendNotificatioToAdminResponse>, t: Throwable) {
                println("Notification send"+t.message)
            }
        })
    }
}

