package com.walterlauk.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TruckTrailerData() :Parcelable {

    @SerializedName("Truck")
    @Expose
    var Truck: ArrayList<TruckTrailer>? = null

    @SerializedName("Trailer")
    @Expose
    var Trailer: ArrayList<TruckTrailer>? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TruckTrailerData> {
        override fun createFromParcel(parcel: Parcel): TruckTrailerData {
            return TruckTrailerData(parcel)
        }

        override fun newArray(size: Int): Array<TruckTrailerData?> {
            return arrayOfNulls(size)
        }
    }
}