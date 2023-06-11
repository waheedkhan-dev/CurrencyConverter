package com.codecollapse.currencyconverter.data.model.currency

import android.os.Parcel
import android.os.Parcelable


data class CommonCurrency(
    val code: String,
    val name: String,
    val symbol: String,
    val isoCode : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeString(isoCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommonCurrency> {
        override fun createFromParcel(parcel: Parcel): CommonCurrency {
            return CommonCurrency(parcel)
        }

        override fun newArray(size: Int): Array<CommonCurrency?> {
            return arrayOfNulls(size)
        }
    }
}