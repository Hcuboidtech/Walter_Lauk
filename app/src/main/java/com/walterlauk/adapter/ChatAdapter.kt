package com.walterlauk.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.walterlauk.R
import com.walterlauk.activity.ChatActivity
import com.walterlauk.activity.ImageFullScreenActivity
import com.walterlauk.activity.OnItemClickedListeners
import com.walterlauk.models.Test

class ChatAdapter(
    var arrayListFirebaseMessages: ArrayList<Test>,
    var context: Activity,
    var listener: OnItemClickedListeners
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var chatActivity:ChatActivity = context as ChatActivity

    var textArrayList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.chat_item_view, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatAdapter.ViewHolder).bindData(position)
        // println("position  -> data"+arrayList[position])
          when(arrayListFirebaseMessages[position].type){
              "text" ->{
                  if (arrayListFirebaseMessages[position].sender_email == "admin_123"){
                      holder.receiver_text.setText(arrayListFirebaseMessages[position].content)
                      holder.receiver_text.visibility = View.VISIBLE
                      holder.sender_text.visibility = View.GONE
                      holder.sender_videoView.visibility =View.GONE
                      holder.sender_image_view.visibility =View.GONE
//                      holder.linearLayout_Video_View_sender.visibility =View.GONE
//                      holder.linearLayout_video_view_receiver.visibility =View.GONE
                  }else{
//                      holder.linearLayout_Video_View_sender.visibility =View.GONE
//                      holder.linearLayout_video_view_receiver.visibility =View.GONE
                      holder.receiver_text.visibility = View.GONE
                      holder.sender_text.setText(arrayListFirebaseMessages[position].content)
                      holder.sender_text.visibility = View.VISIBLE
                      holder.sender_videoView.visibility =View.GONE
                      holder.receiver_videoView.visibility =View.GONE
                      holder.sender_image_view.visibility =View.GONE
                      holder.receiver_image_View.visibility =View.GONE

                 }
              }
              "video" ->{
                  if (arrayListFirebaseMessages[position].sender_email == "admin_123"){
                      holder.sender_text.visibility = View.GONE
//                      holder.linearLayout_Video_View.visibility =View.VISIBLE
                      holder.receiver_videoView.visibility =View.VISIBLE
//                      holder.linearLayout_Video_View_sender.visibility =View.GONE
//                      holder.linearLayout_video_view_receiver.visibility =View.VISIBLE
                      holder.receiver_videoView.setVideoPath(arrayListFirebaseMessages[position].content)
                      val mediaController = MediaController(context)
                      holder.receiver_videoView.setMediaController(mediaController)
                      mediaController.setAnchorView(holder.receiver_videoView)
                      holder.sender_videoView.visibility =View.GONE

                  }else{
                      val mediaController = MediaController(context)
                      holder.sender_videoView.setMediaController(mediaController)
                      mediaController.setAnchorView(holder.receiver_videoView)
                      holder.sender_videoView.visibility =View.VISIBLE
                      holder.sender_videoView.setVideoPath(arrayListFirebaseMessages[position].content)
                      holder.receiver_videoView.visibility =View.GONE
                      holder.sender_text.visibility = View.GONE
//                      holder.linearLayout_Video_View_sender.visibility =View.VISIBLE
//                      holder.linearLayout_video_view_receiver.visibility =View.GONE
                  }
              }
              "photo" ->{
                  if (arrayListFirebaseMessages[position].sender_email == "admin_123") {
                      Picasso.get().load(arrayListFirebaseMessages[position].content).into(holder.receiver_image_View)
                      holder.receiver_image_View.visibility = View.VISIBLE
                      holder.sender_image_view.visibility = View.GONE
                      holder.sender_videoView.visibility =View.GONE
                      holder.sender_text.visibility = View.GONE
                      holder.receiver_text.visibility =View.GONE
//                      holder.linearLayout_Video_View_sender.visibility =View.GONE
//                      holder.linearLayout_video_view_receiver.visibility =View.GONE
                      holder.receiver_image_View.setOnClickListener {
                         context.startActivity(Intent(context,ImageFullScreenActivity::class.java).putExtra("image",arrayListFirebaseMessages[position].content))

                      }
                      }
                  else{
                      Picasso.get().load(arrayListFirebaseMessages[position].content).into(holder.sender_image_view)
                      holder.receiver_image_View.visibility = View.GONE
                      holder.sender_text.visibility = View.GONE
                      holder.sender_videoView.visibility =View.GONE
                      holder.receiver_text.visibility =View.GONE
//                      holder.linearLayout_Video_View_sender.visibility =View.GONE
//                      holder.linearLayout_video_view_receiver.visibility =View.GONE
                      holder.sender_image_view.visibility = View.VISIBLE
                      holder.sender_videoView.visibility =View.GONE
                      holder.sender_image_view.setOnClickListener{
                          context.startActivity(Intent(context,ImageFullScreenActivity::class.java).putExtra("image",arrayListFirebaseMessages[position].content))
                      }
                    }

              }
          }


    }

    override fun getItemCount(): Int {
      return arrayListFirebaseMessages.size
    }
    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){

        var sender_text = view.findViewById<TextView>(R.id.sender_textView)
        var inputTextData = view.findViewById<EditText>(R.id.input_chat_text)
        var btn_send_chat = view.findViewById<ImageView>(R.id.btn_send_chat)
        var sender_image_view = view.findViewById<ImageView>(R.id.sender_imageview)
        var sender_videoView = view.findViewById<VideoView>(R.id.sender_videoView)
        var receiver_videoView = view.findViewById<VideoView>(R.id.receiver_videoView)
        var receiver_text = view.findViewById<TextView>(R.id.receiver_textView)
        var receiver_image_View = view.findViewById<ImageView>(R.id.receiver_imageView)
//        var linearLayout_Video_View_sender =view.findViewById<LinearLayout>(R.id.linearLayout_videoView_sender)
//        var linearLayout_video_view_receiver = view.findViewById<LinearLayout>(R.id.linearLayout_videoView_receiver)
        fun bindData(position: Int) {

//               btn_send_chat.setOnClickListener {
//               println("BUTTON CLICKED  ->  send  chat BUTTON ")
//               val str = inputTextData.getText().toString()
//                sender_text.visibility =View.VISIBLE
//                textArrayList.add(str)
//                sender_text.text = str
           }
        }

    }

// using for the local uploading

//  if (arrayList[position].text1 != "" || arrayList[position].imageUri1 != "") {
//            println("_>> check ->"+chatActivity.imageEnabled)
//            if (arrayList[position].isImageEnabled1){
//                holder.sender_image_view.setImageURI(Uri.parse(arrayList[position].imageUri1))
//                holder.sender_image_view.visibility = View.VISIBLE
//            }else{
//               holder.sender_text.setText(arrayList[position].text1)
//                holder.sender_text.visibility = View.VISIBLE
//            }
//            chatActivity.imageEnabled = 1
//        }