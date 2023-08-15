package com.vallem.sylph.shared.map.model

import android.os.Parcel
import android.os.Parcelable
import com.mapbox.geojson.Point

@JvmInline
value class PointWrapper(val value: Point) : Parcelable {
    constructor(parcel: Parcel) : this(Point.fromJson(parcel.readString()!!))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeString(value.toJson())

    companion object CREATOR : Parcelable.Creator<PointWrapper> {
        override fun createFromParcel(parcel: Parcel) = PointWrapper(parcel)
        override fun newArray(size: Int) = arrayOfNulls<PointWrapper?>(size)
    }
}
