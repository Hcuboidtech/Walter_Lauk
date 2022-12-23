package com.walterlauk.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walterlauk.utils.Constants.Companion.PARENT

class TruckTrailer() :Parcelable {

    @SerializedName("category_id")
    @Expose
    var category_id: Int? = null

    @SerializedName("category_name")
    @Expose
    var category_name: String? = null

    @SerializedName("parts")
    @Expose
    var parts: ArrayList<PartData>? = null

    var isPartAdded:Boolean=false

    var type:Int = PARENT
    var isExpanded:Boolean = false

    constructor(parcel: Parcel) : this() {
        category_id = parcel.readValue(Int::class.java.classLoader) as? Int
        category_name = parcel.readString()
        type = parcel.readInt()
        isExpanded = parcel.readByte() != 0.toByte()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }

    companion object CREATOR : Parcelable.Creator<TruckTrailer> {
        override fun createFromParcel(parcel: Parcel): TruckTrailer {
            return TruckTrailer(parcel)
        }

        override fun newArray(size: Int): Array<TruckTrailer?> {
            return arrayOfNulls(size)
        }
    }
}

