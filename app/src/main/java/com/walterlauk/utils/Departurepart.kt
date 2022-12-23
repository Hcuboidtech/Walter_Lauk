package com.walterlauk.utils

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Departurepart(
    ):Parcelable{
    @SerializedName("category_departure_id")
    @Expose
    var category_departure_id: String?  = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("images")
    @Expose
    var images: String? = null

    @SerializedName("is_part_default")
    @Expose
    var is_part_default: String? = null

    @SerializedName("notes")
    @Expose
    var notes: String? = null

    @SerializedName("part_id")
    @Expose
    var part_id: String? =  null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        part_id = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
        notes = parcel.readString()
        images = parcel.readString()
        is_part_default = parcel.readString()
        category_departure_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(part_id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(images)
        parcel.writeString(is_part_default)
        parcel.writeString(category_departure_id)
        parcel.writeString(notes)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Departurepart> {
        override fun createFromParcel(parcel: Parcel): Departurepart {
            return Departurepart(parcel)
        }

        override fun newArray(size: Int): Array<Departurepart?> {
            return arrayOfNulls(size)
        }
    }

}