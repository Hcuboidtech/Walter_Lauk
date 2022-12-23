package com.walterlauk.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walterlauk.utils.Departurepart

class PartData() :Parcelable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("truck_trailer")
    @Expose
    var truck_trailer: String? = null

    @SerializedName("part_name")
    @Expose
    var part_name: String? = null

    @SerializedName("russian")
    @Expose
    var russian: String? = null

    @SerializedName("serbian")
    @Expose
    var serbian: String? = null

    @SerializedName("german")
    @Expose
    var german: String? = null

    @SerializedName("select_category")
    @Expose
    var select_category: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("departurepart")
    @Expose
    var departurepart:Departurepart?=null

    @SerializedName("damagepart")
    @Expose
    var damagePart:Departurepart? = null

    var isPartAdded:Boolean=false

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        truck_trailer = parcel.readString()
        part_name = parcel.readString()
        russian = parcel.readString()
        serbian = parcel.readString()
        german = parcel.readString()
        select_category = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
//       departurepart?.forEach{
//           it.part_id = parcel.readString()!!
//       }
    }

    override fun describeContents(): Int {

        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeValue(id)
        p0.writeString(truck_trailer)
        p0.writeString(part_name)
        p0.writeString(russian)
        p0.writeString(serbian)
        p0.writeString(german)
        p0.writeString(select_category)
        p0.writeString(created_at)
        p0.writeString(updated_at)
//        p0.writeList(departurepart)
        }

    companion object CREATOR : Parcelable.Creator<PartData> {
        override fun createFromParcel(parcel: Parcel): PartData {
            return PartData(parcel)
        }

        override fun newArray(size: Int): Array<PartData?> {
            return arrayOfNulls(size)
        }
    }
}

